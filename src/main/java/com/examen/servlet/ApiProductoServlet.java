package com.examen.servlet;

import com.examen.dao.ProductoDAO;
import com.examen.model.Producto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet API que responde en JSON o XML según el header Accept.
 * 
 * TEMAS: JSON/XML + Uso de Servlet
 * 
 * Endpoints:
 *   GET /api/productos         → Lista todos (JSON o XML)
 *   GET /api/productos?id=X    → Detalle de uno (JSON o XML)
 */
@WebServlet(name = "ApiProductoServlet", urlPatterns = {"/api/productos"})
public class ApiProductoServlet extends HttpServlet {

    private ProductoDAO dao;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        dao = new ProductoDAO();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accept = request.getHeader("Accept");
        String idParam = request.getParameter("id");

        try {
            if (accept != null && accept.contains("application/xml")) {
                // === RESPUESTA EN XML ===
                response.setContentType("application/xml;charset=UTF-8");
                PrintWriter out = response.getWriter();

                if (idParam != null) {
                    Producto producto = dao.buscarPorId(Integer.parseInt(idParam));
                    if (producto != null) {
                        out.println(productoAXml(producto));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        out.println("<error>Producto no encontrado</error>");
                    }
                } else {
                    List<Producto> productos = dao.listarTodos();
                    out.println(listaAXml(productos));
                }

            } else {
                // === RESPUESTA EN JSON (default) ===
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();

                if (idParam != null) {
                    Producto producto = dao.buscarPorId(Integer.parseInt(idParam));
                    if (producto != null) {
                        out.println(gson.toJson(producto));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.println("{\"error\": \"Producto no encontrado\"}");
                    }
                } else {
                    List<Producto> productos = dao.listarTodos();
                    out.println(gson.toJson(productos));
                }
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // ==================== Conversión manual a XML ====================

    /**
     * Convierte un Producto a XML de forma manual.
     * En un proyecto real se usaría JAXB o similar.
     */
    private String productoAXml(Producto p) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<producto>\n");
        xml.append("  <id>").append(p.getId()).append("</id>\n");
        xml.append("  <nombre>").append(escaparXml(p.getNombre())).append("</nombre>\n");
        xml.append("  <descripcion>").append(escaparXml(p.getDescripcion())).append("</descripcion>\n");
        xml.append("  <precio>").append(p.getPrecio()).append("</precio>\n");
        xml.append("  <stock>").append(p.getStock()).append("</stock>\n");
        xml.append("  <categoria>").append(escaparXml(p.getCategoria())).append("</categoria>\n");
        xml.append("  <activo>").append(p.isActivo()).append("</activo>\n");
        xml.append("</producto>");
        return xml.toString();
    }

    private String listaAXml(List<Producto> productos) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<productos>\n");
        for (Producto p : productos) {
            xml.append("  <producto>\n");
            xml.append("    <id>").append(p.getId()).append("</id>\n");
            xml.append("    <nombre>").append(escaparXml(p.getNombre())).append("</nombre>\n");
            xml.append("    <descripcion>").append(escaparXml(p.getDescripcion())).append("</descripcion>\n");
            xml.append("    <precio>").append(p.getPrecio()).append("</precio>\n");
            xml.append("    <stock>").append(p.getStock()).append("</stock>\n");
            xml.append("    <categoria>").append(escaparXml(p.getCategoria())).append("</categoria>\n");
            xml.append("    <activo>").append(p.isActivo()).append("</activo>\n");
            xml.append("  </producto>\n");
        }
        xml.append("</productos>");
        return xml.toString();
    }

    private String escaparXml(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
