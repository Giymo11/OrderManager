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

        if (request.getSession().getAttribute("loggedIn") != null) {

            loggedIn = (boolean) request.getSession().getAttribute("loggedIn");
            System.out.println("loggedIn set to true");
        }


        if (request.getRequestURI().equals(request.getContextPath() + "/faces/login.xhtml")) {
            if (loggedIn) {
                System.out.println(request.getContextPath());
                response.sendRedirect(request.getContextPath() + "/faces/orders.xhtml");
                System.out.println("Weiterleitung zu orders.xhtml");
                return;
            } else {
                System.out.println("Noch nicht eingeloggt");
                chain.doFilter(req, resp);
                return;
            }
        }


        System.out.println("Filter nicht genutzt.");
        chain.doFilter(req, resp);
    }


    public void init(FilterConfig config) throws ServletException {
        System.out.println("Filter Initialized");
        this.fc = config;
    }

}
