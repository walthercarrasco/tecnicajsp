package com.examen.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet para manejar preferencias de usuario mediante Cookies.
 * 
 * TEMA: Cookies
 * 
 * Maneja:
 *   POST /preferencias?tema=oscuro  → Guarda preferencia de tema en cookie
 *   POST /preferencias?items=10     → Guarda items por página en cookie
 */
@WebServlet(name = "PreferenciasServlet", urlPatterns = {"/preferencias"})
public class PreferenciasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tema = request.getParameter("tema");
        String itemsPorPagina = request.getParameter("items");

        // === COOKIE DE TEMA (claro/oscuro) ===
        if (tema != null) {
            Cookie cookieTema = new Cookie("tema_preferido", tema);
            cookieTema.setMaxAge(30 * 24 * 60 * 60); // 30 días
            cookieTema.setPath(request.getContextPath());
            response.addCookie(cookieTema);
        }

        // === COOKIE DE ITEMS POR PÁGINA ===
        if (itemsPorPagina != null) {
            Cookie cookieItems = new Cookie("items_por_pagina", itemsPorPagina);
            cookieItems.setMaxAge(30 * 24 * 60 * 60); // 30 días
            cookieItems.setPath(request.getContextPath());
            response.addCookie(cookieItems);
        }

        // Responder con JSON para llamadas AJAX
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{\"status\": \"ok\", \"tema\": \"" + tema + "\"}");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Leer preferencias actuales de las cookies
        String tema = "claro";
        String items = "10";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("tema_preferido".equals(cookie.getName())) {
                    tema = cookie.getValue();
                }
                if ("items_por_pagina".equals(cookie.getName())) {
                    items = cookie.getValue();
                }
            }
        }

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{\"tema\": \"" + tema + "\", \"items\": \"" + items + "\"}");
    }
}
