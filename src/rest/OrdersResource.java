package rest;

import dao.OrderDao;

import javax.ws.rs.*;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 28.04.14
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */

@Path("/orders/{id}")
@Produces("application/json")
public class OrdersResource {
    private OrderDao orderdao;

    public OrdersResource(){
        orderdao = new OrderDao();
    }

    @GET
    public List getOrders(@PathParam("id")int addressid){
        return orderdao.getOrdersByAddress(addressid);
    }

    @POST
    @Consumes("/application/json")
    @Path("/{addressid}")
    public void setMemo(@PathParam("addressid")int addressID, String memo){
        orderdao.writeMemoWithAddressID(addressID, new Date(), memo);
    }
}
