package com.examen.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Filtro de autenticación que protege las rutas del sistema.
 * 
 * TEMAS: Uso de Servlet (Filtros) + Variables de Sesión
 * 
 * Permite acceso libre a:
 *   - /login
 *   - /api/* (API pública)
 *   - /css/*, /js/* (recursos estáticos)
 * 
 * Todas las demás rutas requieren sesión activa.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No requiere inicialización
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        // Rutas que NO requieren autenticación
        boolean esRutaPublica = path.equals("/login")
                || path.equals("/")
                || path.startsWith("/api/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".ico");

        if (esRutaPublica) {
            chain.doFilter(request, response);
            return;
        }

        // Verificar sesión para rutas protegidas
        HttpSession session = request.getSession(false);
        boolean estaAutenticado = (session != null && session.getAttribute("usuario") != null);

        if (estaAutenticado) {
            chain.doFilter(request, response);
        } else {
            // Guardar la URL original para redirigir después del login
            session = request.getSession(true);
            session.setAttribute("urlOriginal", uri);
            response.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void destroy() {
        // No requiere limpieza
    }
}
