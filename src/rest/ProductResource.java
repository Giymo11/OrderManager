package rest;

import dao.ProductDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 28.04.14
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
@Path("/products")
@Produces("application/json")
public class ProductResource {
    private ProductDao productDao;

    public ProductResource(){
        productDao=new ProductDao();
    }

    @GET
    public List getProducts(){
        return productDao.getProductList();
    }
}
