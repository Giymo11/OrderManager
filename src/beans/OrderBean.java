package beans;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.TourDAO;
import dto.Order;
import dto.OrderItem;
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
    private Date date;
    private String memo;
    private int currentOrderid;
    private List<OrderItem> allOrdered;
    private List<OrderItem> orderItemsForDate;

    public OrderBean(){
        orderItemDAO = new OrderItemDAO();
        orderDAO = new OrderDAO();
        tourDAO = new TourDAO();
        newOrderItems = new ArrayList<>();
        setDate(getNextDay());
        allOrdered = new ArrayList<>();
        newOrderItems = new ArrayList<>();
        sumOrders();
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

        return "/orders.xhtml?faces-redirect=true";
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

    public String getDate(int orderid){
        Date date = new Date();

        int tourID = orderDAO.getTourIDWithID(orderid);
        if(tourID != -1)
            date = tourDAO.getDateWithID(tourID);

        return (date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" + date.getDate();
    }

    public List<Order> getOrderList(){
        return orderDAO.getOrderList();
    }

    public List<OrderItem> getAllItemsWithID(int orderid){
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
}
