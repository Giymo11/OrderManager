package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 20.02.14
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */

@WebFilter
public class PermissionFilter implements Filter {
    private FilterConfig fc;

    public void destroy() {
        this.fc = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        boolean loggedIn = false;
        boolean adminLoggedIn = false;

        if (request.getSession().getAttribute("loggedIn") != null) {
            loggedIn = (boolean) request.getSession().getAttribute("loggedIn");
        }

        if(request.getSession().getAttribute("adminLoggedIn")!=null){
            adminLoggedIn = (boolean) request.getSession().getAttribute("adminLoggedIn");
        }

        if (request.getRequestURI().equals(request.getContextPath() + "/faces/login.xhtml")) {
            if (loggedIn) {
                response.sendRedirect(request.getContextPath() + "/faces/ordersForCustomer.xhtml");
                return;
            }
            if(adminLoggedIn){
                response.sendRedirect(request.getContextPath() + "/faces/ordersForAdmin.xhtml");
                return;
            }
             else {
                chain.doFilter(req, resp);
                return;
            }


        }

        if(request.getRequestURI().equals(request.getContextPath() + "/faces/settings.xhtml")){
            if(loggedIn){
                response.sendRedirect(request.getContextPath() + "/faces/settingsCustomer.xhtml");
                return;
            }
            if(adminLoggedIn){
                chain.doFilter(req, resp);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/faces/login.xhtml");
            return;
        }


        if(request.getRequestURI().equals(request.getContextPath() + "/faces/productsNL.xhtml")){
            if(loggedIn){
                response.sendRedirect(request.getContextPath() + "/faces/products.xhtml");
                return;
            }
            if(adminLoggedIn){
                response.sendRedirect(request.getContextPath() + "/faces/products.xhtml");
                return;
            }
            chain.doFilter(req, resp);
            return;
        }





        chain.doFilter(req, resp);
    }


    public void init(FilterConfig config) throws ServletException {
        this.fc = config;
    }

}
