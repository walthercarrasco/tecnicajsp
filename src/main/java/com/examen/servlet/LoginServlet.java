package com.examen.servlet;

import com.examen.dao.ProductoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet para manejar el inicio de sesión.
 * 
 * TEMAS: Uso de Servlet + Variables de Sesión + Cookies
 * 
 * - POST /login → Valida credenciales, crea sesión, opcionalmente guarda cookie
 * - GET  /login → Muestra el formulario de login
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private ProductoDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new ProductoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si ya está logueado, redirigir al inicio
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        // Verificar si hay cookie de "recordar usuario"
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("recordar_usuario".equals(cookie.getName())) {
                    request.setAttribute("usuarioRecordado", cookie.getValue());
                    break;
                }
            }
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String recordar = request.getParameter("recordar"); // checkbox

        try {
            // Validar credenciales contra la base de datos
            String[] datosUsuario = dao.validarUsuario(username, password);

            if (datosUsuario != null) {
                // === VARIABLES DE SESIÓN ===
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", username);
                session.setAttribute("nombreCompleto", datosUsuario[0]);
                session.setAttribute("rol", datosUsuario[1]);
                session.setMaxInactiveInterval(30 * 60); // 30 minutos

                // === COOKIES ===
                if ("on".equals(recordar)) {
                    // Cookie para recordar el nombre de usuario (7 días)
                    Cookie cookieUsuario = new Cookie("recordar_usuario", username);
                    cookieUsuario.setMaxAge(7 * 24 * 60 * 60); // 7 días en segundos
                    cookieUsuario.setPath(request.getContextPath());
                    response.addCookie(cookieUsuario);
                } else {
                    // Eliminar cookie si no marcó "recordar"
                    Cookie cookieUsuario = new Cookie("recordar_usuario", "");
                    cookieUsuario.setMaxAge(0); // Eliminar
                    cookieUsuario.setPath(request.getContextPath());
                    response.addCookie(cookieUsuario);
                }

                // Redirigir al listado de productos
                response.sendRedirect(request.getContextPath() + "/productos");

            } else {
                // Credenciales inválidas
                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error interno: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
