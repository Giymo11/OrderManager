package beans;

import dao.OrderDao;
import dao.OrderItemDao;
import dao.ProductDao;
import dao.TourDAO;
import dto.Order;
import dto.OrderItem;
import dto.Product;
import dto.Tour;
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
public class OrderService {
    private OrderItemDao orderItemDAO;
    private List<OrderItem> newOrderItems;
    private TourDAO tourDAO;
    private OrderDao orderDao;
    private int productID;
    private Date date, startDate, endDate;
    private String memo;
    private int currentOrderid;

    private List<OrderItem> allOrdered;
    private List<OrderItem> orderItemsForDate;
    private List<Order> ordersInDateRange;
    private List<Order> orderList;
    private ProductDao productDAO;
    private List<Tour> tourList;
    private List<OrderItem> allItemsForDate;

    public OrderService(){
        orderItemDAO = new OrderItemDao();
        orderDao = new OrderDao();
        tourDAO = new TourDAO();
        orderList = orderDao.getOrderList();
        newOrderItems = new ArrayList();
        ordersInDateRange = new ArrayList();
        allOrdered = new ArrayList();
        newOrderItems = new ArrayList();
        orderItemsForDate = new ArrayList();
        tourList = tourDAO.getTourList();

        setDate(getNextDay());

        startDate = new Date();
        endDate = new Date();
        endDate.setDate(startDate.getDate()+1);

        productDAO = new ProductDao();
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

        int tourID = getTourIDWithDate(date);

        if(tourID==-1){
            tourList.add(tourDAO.addTour(date));
            tourID = getTourIDWithDate(date);
        }

        currentOrderid = getOrderID(tourID, email);

        if(currentOrderid == -1){
            Order order = orderDao.addOrder(tourID, email, memo);
            orderList.add(order);
            currentOrderid = order.getId();
        }
        else
            if(!memo.equals(""))
                orderDao.writeMemoWithID(currentOrderid, memo, "Pock");

        addOrderIDToItems(currentOrderid);

        for(OrderItem item : newOrderItems)
            orderItemDAO.addOrderItem(item);

        newOrderItems = new ArrayList();

        return "/ordersForCustomer.xhtml?faces-redirect=true";
    }

    private int getTourIDWithDate(Date date){
        if(!tourList.isEmpty())
            for(Tour tour : tourList)
                if(tour.getDate().equals(date))
                    return tour.getId();

        FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Achtung! Für diesen Tag gibt es noch keine eingetragene Tour"));
        return -1;
    }

    private void addOrderIDToItems(int orderid) {
        for(OrderItem item : newOrderItems)
            item.setOrderid(orderid);
    }

    public void addOneOrderItem(int quantity){
        if(quantity>0) {
            productID = Integer.parseInt(fetchParameter("productID"));

            for (OrderItem orderItem : newOrderItems) {
                if (orderItem.getProductid() == productID) {
                    quantity += orderItem.getOrdered();
                    orderItem.setOrdered(quantity);
                    return;
                }
            }
            OrderItem orderItem = new OrderItem(productID, quantity, -1);
            orderItem.setId(getNextId(newOrderItems));
            newOrderItems.add(orderItem);
        }
    }

    private int getNextId(List<OrderItem> orderItems) {
        int id = 1;
        for(OrderItem item : orderItems)
            if(item.getId() >= id)
                id=item.getId()+1;
        return id;
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = (String) req.getSession().getAttribute("email");
        orderItemsForDate = orderItemDAO.getOrderItemsForDate(date, email);

        this.date = date;
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
        allOrdered = orderItemDAO.getAllOrderItemsForUser(email);

        return allOrdered;
    }

    public void handleDateSelect(SelectEvent event){
        setDate((Date) event.getObject());
    }

    public String redirectToOrders(){
        return "/order.xhtml?faces-redirect=true";
    }

    public void deleteOneOrderItem(){
        int id = Integer.parseInt(fetchParameter("orderItemID"));
        for(int i=0; i< newOrderItems.size(); i++){
            if(newOrderItems.get(i).getId()==id) {
                newOrderItems.remove(i);
            }
        }

        orderItemDAO.deleteOneItem(id);
    }

    public String getDateWithOrderID(int orderid){
        Date date = new Date();

        int tourID = getTourIDWithID(orderid);
        if(tourID != -1)
            date = getDateWithID(tourID);

        return (date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" + date.getDate();
    }

    private Date getDateWithID(int tourID) {
        for(Tour tour : tourList)
            if(tour.getId() == tourID)
                return tour.getDate();
        return null;
    }

    public List<OrderItem> getAllItemsWithIDForCurrentUser(int orderid){
        List<OrderItem> orderItems = new ArrayList();

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
        ordersInDateRange = orderDao.getOrdersInDateRange(startDate, endDate, email);
    }

    public String sumUp(float price, int quantity){
        float sum = (float) Math.round(price*quantity * 100) / 100;
        String sumStr = sum + "";
        String string = sumStr.substring(sumStr.indexOf('.'));

        if(string.length()<=2)
            sumStr += "0";

        return sumStr;
    }

    public String getSumAll(List<OrderItem> items){
        float sum = 0.0f;
        for (OrderItem item : items ) {
            sum += item.getOrdered() * getPrice(item.getProductid());
        }

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

    public int getTourIDWithID(int orderid) {
        for(Order order : orderList){
            if(order.getId() == orderid)
                return order.getTourid();
        }
        return -1;
    }

    public int getOrderID(int tourID, String email) {
        int addressID = orderDao.getAddressIDForEmail(email);

        for (Order order : orderList) {
            if (order.getTourid() == tourID && order.getAddressid() == addressID)
                return order.getId();
        }

        return -1;
    }

    public List<OrderItem> getOrderItemsForDate(){
        return orderItemsForDate;
    }

    public List<OrderItem> getNewOrderItems() {
        return newOrderItems;
    }

    public String getOrderDate() {
        return startDate.getDate() + "." + (startDate.getMonth()+1) + "." + (startDate.getYear()+1900);
    }

    public List<Order> getOrdersForDate() {
        return orderDao.getOrdersByStatus(startDate, false);
    }

    public void writeMemo(String memo) {
        int id = Integer.parseInt(fetchParameter("orderMemo"));
        orderDao.writeMemoWithID(id, memo, "Customer");
    }

    public List<OrderItem> getAllItemsForDate(){
        allItemsForDate = orderItemDAO.getAllItemsForDate(startDate);
        return allItemsForDate;
    }
}
