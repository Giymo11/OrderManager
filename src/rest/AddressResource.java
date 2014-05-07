package rest;

import dao.AddressDao;
import dto.Address;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 28.04.14
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */

@Path("/addresses/{id}")
@Produces("application/json")
public class AddressResource {
    public static Map<Integer, Integer> addressMap;
    private AddressDao addressDao;

    public AddressResource() {
        addressDao = new AddressDao();
        addressMap = new HashMap();
    }

    @POST
    @Consumes("application/json")
    public void setAddressID(Address address) {

        System.out.println("works? - " + address);

        if (address.getId() < 0) {
            int negativeID = address.getId();
            addressDao.writeAddress(address);
            addressMap.put(negativeID, address.getId());
        }

        for (Map.Entry<Integer, Integer> entry : addressMap.entrySet())
            System.out.println("negative: " + entry.getKey() + " - positive: " + entry.getValue());
    }

    @GET
    public List getAddresses(@PathParam("id") int townid) {
        return addressDao.getAddressesWithTownID(townid);
    }


}
