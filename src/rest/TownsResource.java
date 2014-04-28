package rest;

import dao.TownDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 28.04.14
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
@Path("/towns")
@Produces("application/json")
public class TownsResource {

    private TownDao towndao;

    public TownsResource(){
        towndao = new TownDao();

    }

    @GET
    public List getTowns(){
        return towndao.getTowns();
    }

}
