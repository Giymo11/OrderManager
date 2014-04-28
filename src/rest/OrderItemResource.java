package rest;

import dao.AddressDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dto.OrderItem;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    @Consumes("/application/json")
    @Path("/{addressid}")
    public void storeOrderItems(@PathParam("addressid")int addressID, Collection orderitems){
        List<OrderItem> orderItems = (ArrayList) orderitems;
        int orderID;
        for(OrderItem item : orderItems){
            if(item.getId()>0){
                orderItemDao.update(item);
            }
            else{
                if(addressID<0){
                    int realID = AddressResource.addressMap.get(addressID);
                    orderID = orderDao.addOrderWithAddressID(realID, new Date());
                    item.setOrderid(orderID);
                }
                orderItemDao.insertItem(item, addressID);
            }
        }

    }

    @GET
    public List getOrderItems(@PathParam("id")int orderID){
        return orderItemDao.getAllItemsForOrder(orderID);
    }

}
