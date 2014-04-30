package rest;

import dao.CategoryDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 28.04.14
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */

@Path("/categories")
@Produces("application/json;charset=utf-8")
public class CategoriesResource {
    private CategoryDao categoryDao;

    public CategoriesResource(){
        categoryDao = new CategoryDao();
    }

    @GET
    public List getCategories(){
        return categoryDao.getCategories();
    }

}
