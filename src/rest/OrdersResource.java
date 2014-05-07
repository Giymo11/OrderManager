package rest;

import dao.OrderDao;
import dto.Order;

import javax.ws.rs.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 28.04.14
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */

@Path("/orders/{id}")
public class OrdersResource {
    private OrderDao orderdao;

    public OrdersResource(){
        orderdao = new OrderDao();
    }

    @GET
    @Produces("application/json")
    public Order getOrders(@PathParam("id") int addressid) {
        return orderdao.getOrderByAddressForCurrentDay(addressid);
    }

    @POST
    @Consumes("text/plain")
    public void setMemo(@PathParam("id") int addressID, String memo) {
        System.out.println("Memo: " + memo);
        if (addressID < 0)
            addressID = AddressResource.addressMap.get(addressID);
        orderdao.writeMemoWithAddressID(addressID, new Date(), memo);
    }
}
