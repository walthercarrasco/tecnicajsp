package com.examen.servlet;

import com.examen.dao.ProductoDAO;
import com.examen.model.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet principal para operaciones CRUD de productos.
 * 
 * TEMAS: Uso de Servlet + CRUD Java
 * 
 * Rutas manejadas:
 *   GET  /productos              → Listar todos los productos
 *   GET  /productos?accion=nuevo → Formulario para nuevo producto
 *   GET  /productos?accion=editar&id=X  → Formulario para editar
 *   GET  /productos?accion=detalle&id=X → Ver detalle
 *   GET  /productos?accion=eliminar&id=X → Eliminar producto
 *   POST /productos              → Guardar producto (crear o actualizar)
 */
@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    private ProductoDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new ProductoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            switch (accion) {
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "detalle":
                    mostrarDetalle(request, response);
                    break;
                case "eliminar":
                    eliminarProducto(request, response);
                    break;
                case "buscar":
                    buscarProductos(request, response);
                    break;
                default:
                    listarProductos(request, response);
                    break;
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("/productos/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            guardarProducto(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error al guardar: " + e.getMessage());
            request.getRequestDispatcher("/productos/formulario.jsp").forward(request, response);
        }
    }

    // ==================== LISTAR ====================

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        List<Producto> productos = dao.listarTodos();
        List<String> categorias = dao.listarCategorias();

        request.setAttribute("productos", productos);
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("/productos/listar.jsp").forward(request, response);
    }

    // ==================== BUSCAR ====================

    private void buscarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String termino = request.getParameter("q");
        String categoria = request.getParameter("categoria");
        List<Producto> productos;

        if (categoria != null && !categoria.isEmpty()) {
            productos = dao.buscarPorCategoria(categoria);
        } else if (termino != null && !termino.isEmpty()) {
            productos = dao.buscarPorNombre(termino);
        } else {
            productos = dao.listarTodos();
        }

        List<String> categorias = dao.listarCategorias();

        request.setAttribute("productos", productos);
        request.setAttribute("categorias", categorias);
        request.setAttribute("terminoBusqueda", termino);
        request.setAttribute("categoriaSeleccionada", categoria);
        request.getRequestDispatcher("/productos/listar.jsp").forward(request, response);
    }

    // ==================== FORMULARIO NUEVO ====================

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        List<String> categorias = dao.listarCategorias();
        request.setAttribute("categorias", categorias);
        request.setAttribute("titulo", "Nuevo Producto");
        request.getRequestDispatcher("/productos/formulario.jsp").forward(request, response);
    }

    // ==================== FORMULARIO EDITAR ====================

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int id = Integer.parseInt(request.getParameter("id"));
        Producto producto = dao.buscarPorId(id);

        if (producto == null) {
            request.setAttribute("error", "Producto no encontrado con ID: " + id);
            listarProductos(request, response);
            return;
        }

        List<String> categorias = dao.listarCategorias();
        request.setAttribute("producto", producto);
        request.setAttribute("categorias", categorias);
        request.setAttribute("titulo", "Editar Producto");
        request.getRequestDispatcher("/productos/formulario.jsp").forward(request, response);
    }

    // ==================== DETALLE ====================

    private void mostrarDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int id = Integer.parseInt(request.getParameter("id"));
        Producto producto = dao.buscarPorId(id);

        if (producto == null) {
            request.setAttribute("error", "Producto no encontrado con ID: " + id);
            listarProductos(request, response);
            return;
        }

        request.setAttribute("producto", producto);
        request.getRequestDispatcher("/productos/detalle.jsp").forward(request, response);
    }

    // ==================== GUARDAR (Crear o Actualizar) ====================

    private void guardarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String idParam = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");
        String stockStr = request.getParameter("stock");
        String categoria = request.getParameter("categoria");
        String activoStr = request.getParameter("activo");

        // Validación del lado del servidor
        if (nombre == null || nombre.trim().isEmpty()) {
            request.setAttribute("error", "El nombre es obligatorio");
            request.getRequestDispatcher("/productos/formulario.jsp").forward(request, response);
            return;
        }

        Producto producto = new Producto();
        producto.setNombre(nombre.trim());
        producto.setDescripcion(descripcion != null ? descripcion.trim() : "");
        producto.setPrecio(new BigDecimal(precioStr));
        producto.setStock(Integer.parseInt(stockStr));
        producto.setCategoria(categoria);
        producto.setActivo("on".equals(activoStr) || "true".equals(activoStr));

        if (idParam != null && !idParam.isEmpty()) {
            // ACTUALIZAR
            producto.setId(Integer.parseInt(idParam));
            dao.actualizar(producto);

            // Guardar mensaje en sesión para mostrar después del redirect
            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Producto actualizado exitosamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            // CREAR
            dao.crear(producto);

            HttpSession session = request.getSession();
            session.setAttribute("mensaje", "Producto creado exitosamente");
            session.setAttribute("tipoMensaje", "success");
        }

        // Post-Redirect-Get pattern
        response.sendRedirect(request.getContextPath() + "/productos");
    }

    // ==================== ELIMINAR ====================

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int id = Integer.parseInt(request.getParameter("id"));
        boolean eliminado = dao.eliminar(id);

        HttpSession session = request.getSession();
        if (eliminado) {
            session.setAttribute("mensaje", "Producto eliminado exitosamente");
            session.setAttribute("tipoMensaje", "success");
        } else {
            session.setAttribute("mensaje", "No se pudo eliminar el producto");
            session.setAttribute("tipoMensaje", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/productos");
    }
}
