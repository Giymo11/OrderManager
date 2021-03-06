package rest;

import dao.AddressDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dto.Order;
import dto.OrderItem;

import javax.ws.rs.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 28.04.14
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
@Path("/orderitems")
@Produces("application/json")
public class OrderItemResource {
    private OrderItemDao orderItemDao;
    private AddressDao addressDao;
    private OrderDao orderDao;

    public OrderItemResource(){
        orderItemDao=new OrderItemDao();
        addressDao =new AddressDao();
        orderDao =new OrderDao();
    }

    @POST
    @Consumes("application/json")
    @Path("/{addressid}")
    public void storeOrderItems(@PathParam("addressid")int addressID, Collection orderitems){
        Collection<LinkedHashMap<String, Object>> orderItems = orderitems;
        if(addressID<0){
            addressID = AddressResource.addressMap.get(addressID);
            System.out.println("Address new: " + addressID);
        }

        Order order = orderDao.getOrderByAddressForCurrentDay(addressID);
        int orderID;

        if(order!=null) {
            order.setDelivered(true);
            orderDao.writeDeliveredStatus(order);
            orderID = order.getId();
        }
        else
            orderID = orderDao.addOrderWithAddressID(addressID, new Date());

        for (LinkedHashMap<String, Object> map : orderItems) {
            OrderItem item = getOrderItem(map);
            System.out.println("addressid: " + addressID + ", item - " + item.toString());

            if(item.getId()>0){
                orderItemDao.update(item);
            }
            else{
                if (item.getOrderid() < 0) {
                    item.setOrderid(orderID);
                    System.out.println("orderID: " + orderID + ", addressID: " + addressID);
                }
                orderItemDao.insertItem(item, addressID);
            }
        }
    }

    private OrderItem getOrderItem(HashMap<String, Object> json) {
        int id, orderid, productid, ordered, delivered;

        id = (Integer) json.get("id");
        orderid = (Integer) json.get("orderid");
        productid = (Integer) json.get("productid");
        ordered = (Integer) json.get("ordered");
        delivered = (Integer) json.get("delivered");

        return new OrderItem(id, orderid, productid, ordered, delivered);
    }

    @GET
    @Produces("application/json")
    @Path("/{orderid}")
    public List getOrderItems(@PathParam("orderid") int orderID) {
        return orderItemDao.getAllItemsForOrder(orderID);
    }

}
