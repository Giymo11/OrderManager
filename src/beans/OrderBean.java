package beans;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.TourDAO;
import dto.Order;
import dto.OrderItem;
import dto.Product;
import org.primefaces.event.SelectEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@SessionScoped
public class OrderBean {
    private OrderItemDAO orderItemDAO;
    private List<OrderItem> newOrderItems, orderItems;
    private TourDAO tourDAO;
    private OrderDAO orderDAO;
    private int productID;
    private Date date, startDate, endDate;
    private String memo;
    private int currentOrderid;
    private List<OrderItem> allOrdered;
    private List<OrderItem> orderItemsForDate;
    private List<Order> ordersInDateRange;
    private List<Order> undeliveredOrders;
    private ProductDAO productDAO;

    public OrderBean(){
        orderItemDAO = new OrderItemDAO();
        orderDAO = new OrderDAO();
        tourDAO = new TourDAO();
        newOrderItems = new ArrayList<>();
        setDate(getNextDay());
        allOrdered = new ArrayList<>();
        newOrderItems = new ArrayList<>();
        sumOrders();
        ordersInDateRange = new ArrayList<>();
        startDate = new Date();
        endDate = new Date();
        endDate.setDate(startDate.getDate()+1);
        undeliveredOrders = new ArrayList<>();

        productDAO = new ProductDAO();
    }

    private Date getNextDay() {
        Date date = new Date();
        int dayOfMonth = date.getDate();
        ++dayOfMonth;
        date.setDate(dayOfMonth);

        return date;
    }

    public String addOrder(){
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = (String) req.getSession().getAttribute("email");

        int tourID = tourDAO.getTourIDWithDate(date);

        if(tourID==-1){
            tourDAO.addTour(date);
            tourID = tourDAO.getTourIDWithDate(date);
        }

        orderDAO.addOrder(tourID, email, memo);
        currentOrderid = orderDAO.getOrderID(tourID, email);

        if(currentOrderid != -1){
            addOrderIDToItems(currentOrderid);
        }

        for(OrderItem item : newOrderItems)
            orderItemDAO.addOrderItem(item);

        return "/ordersForCustomer.xhtml?faces-redirect=true";
    }

    private boolean verifyDate(Date date) {
        if(date.before(new Date()))
            return false;

        return true;
    }

    private void addOrderIDToItems(int orderid) {
        for(OrderItem item : newOrderItems)
            item.setOrderid(orderid);
    }

    public void addOneOrderItem(int quantity){
        productID = Integer.parseInt(fetchParameter("productID"));

        for(OrderItem orderItem : newOrderItems){
            if(orderItem.getProductid() == productID){
                quantity += orderItem.getOrdered();
                orderItem.setOrdered(quantity);
                return;
            }
        }
        OrderItem orderItem = new OrderItem(productID, quantity, -1);
        newOrderItems.add(orderItem);
        sumOrders();
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if(verifyDate(date)){
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String email = (String) req.getSession().getAttribute("email");
            orderItemsForDate = orderItemDAO.getOrderItemsForDate(date, email);

            this.date = date;
        }
        else{
            FacesContext.getCurrentInstance().addMessage("Achtung" , new FacesMessage("Bitte wählen Sie ein späteres Datum!"));
        }
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<OrderItem> getAllOrdered(){
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = (String) req.getSession().getAttribute("email");

        allOrdered = orderItemDAO.getOrderForUser(email);

        return allOrdered;
    }

    public void handleDateSelect(SelectEvent event){
        setDate((Date) event.getObject());
        sumOrders();
    }

    private void sumOrders() {
        orderItems = new ArrayList<>();
        for(OrderItem orderItem : newOrderItems)
            orderItems.add(orderItem);
        if(!orderItemsForDate.isEmpty()){
            for(OrderItem orderItem : orderItemsForDate){
                for(OrderItem o : orderItems){
                    if(orderItem.getProductid() == o.getProductid()){
                        int ordered = o.getOrdered() + orderItem.getOrdered();
                        o.setOrdered(ordered);
                        break;
                    }
                }
                orderItems.add(orderItem);
            }
        }
    }

    public String redirectToOrders(){
        return "/order.xhtml?faces-redirect=true";
    }

    public void deleteOneOrderItem(){
        int id = Integer.parseInt(fetchParameter("orderItemID"));

        for(int i=0; i< newOrderItems.size(); i++){
            if(newOrderItems.get(i).getId()==id)
                newOrderItems.remove(i);
        }

        orderItemDAO.deleteOneItem(id);
    }

    public String getDateWithOrderID(int orderid){
        Date date = new Date();

        int tourID = orderDAO.getTourIDWithID(orderid);
        if(tourID != -1)
            date = tourDAO.getDateWithID(tourID);

        return (date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" + date.getDate();
    }

    public List<OrderItem> getAllItemsWithIDForCurrentUser(int orderid){
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItem orderItem : getAllOrdered())
            if(orderItem.getOrderid() == orderid)
                orderItems.add(orderItem);

        return orderItems;
    }

    public Date getMinDate(){
        Date date1 = new Date();
        int day = date1.getDate();
        ++day;
        date1.setDate(day);

        return date1;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Order> getOrdersInDateRange(){
        if(ordersInDateRange.isEmpty())
            setOrdersInDateRange();

        return ordersInDateRange;
    }

    public void handleDateSelectStart(SelectEvent event){
        setStartDate((Date) event.getObject());
        setOrdersInDateRange();
    }

    public void handleDateSelectEnd(SelectEvent event){
        endDate = (Date) event.getObject();
        setOrdersInDateRange();
    }

    private void setOrdersInDateRange(){
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = (String) req.getSession().getAttribute("email");
        ordersInDateRange = orderDAO.getOrdersInDateRange(startDate, endDate, email);
    }

    public List<Order> getUndeliveredOrders(){
        undeliveredOrders = orderDAO.getOrdersByStatus(startDate, false);
        return orderDAO.getOrdersByStatus(startDate, false);
    }

    public void writeMemo(String memo){
        int id = Integer.parseInt(fetchParameter("orderMemo"));
        orderDAO.writeMemoWithID(id, memo);
    }

    public List<OrderItem> getOrderItemSum(){
        List<OrderItem> list = new ArrayList<>();
        int count = 0;
        if(undeliveredOrders.isEmpty())
            getUndeliveredOrders();
        for(Order order : undeliveredOrders)
            for(OrderItem item : getItemsForOrderID(order.getId())) {
                for(OrderItem listItem : list) {
                    if (listItem.getProductid() == item.getProductid()) {
                        listItem.setOrdered(listItem.getOrdered() + item.getOrdered());
                        break;
                    } else{
                        ++count;
                    }
                }
                if(count == list.size()) {
                    list.add(item);
                    count=0;
                }
            }
        return list;
    }

    private List<OrderItem> getItemsForOrderID(int id) {
        return orderItemDAO.getOrderItemsForOrderID(id);
    }

    public String sumUp(float price, int quantity){
        float sum = (float) Math.round(price*quantity * 100) / 100;
        String sumStr = sum + "";
        String string = sumStr.substring(sumStr.indexOf('.'));

        if(string.length()<=2)
            sumStr += "0";

        return sumStr;
    }

    public String getSumAll(){
        List<OrderItem> list = getOrderItemSum();
        float sum = 0.0f;

        for(OrderItem item : list)
            sum += getPrice(item.getProductid())*item.getOrdered();

        String sumStr = sum + "";
        String string = sumStr.substring(sumStr.indexOf('.'));

        if(string.length()<=2)
            sumStr += "0";

        return sumStr;
    }

    private float getPrice(int id){
        for(Product p : productDAO.getProductList())
            if(p.getId() == id)
                return p.getPrice();
        return 0;
    }
}
