package com.examen.dao;

import com.examen.model.Producto;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para operaciones CRUD de Producto.
 * 
 * TEMAS: CRUD Java + Conexión Java a Base de Datos
 * 
 * Usa PreparedStatement para prevenir SQL Injection.
 */
public class ProductoDAO {

    private final ConexionDB conexionDB;

    public ProductoDAO() {
        this.conexionDB = ConexionDB.getInstancia();
    }

    // ==================== CREATE ====================

    /**
     * Inserta un nuevo producto en la base de datos.
     * @return el producto con su ID generado
     */
    public Producto crear(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexionDB.getConexion();
            // Oracle requiere especificar el nombre de columna para claves generadas
            ps = conn.prepareStatement(sql, new String[]{"ID"});

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getCategoria());
            ps.setInt(6, producto.isActivo() ? 1 : 0);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    producto.setId(rs.getInt(1));
                }
            }

            return producto;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    // ==================== READ (uno) ====================

    /**
     * Busca un producto por su ID.
     */
    public Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearProducto(rs);
            }

            return null;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    // ==================== READ (todos) ====================

    /**
     * Lista todos los productos.
     */
    public List<Producto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM productos ORDER BY id DESC";
        List<Producto> productos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

            return productos;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    /**
     * Busca productos por categoría.
     */
    public List<Producto> buscarPorCategoria(String categoria) throws SQLException {
        String sql = "SELECT * FROM productos WHERE categoria = ? ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, categoria);
            rs = ps.executeQuery();

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

            return productos;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    /**
     * Busca productos cuyo nombre contenga el texto dado.
     */
    public List<Producto> buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM productos WHERE LOWER(nombre) LIKE ? ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nombre.toLowerCase() + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

            return productos;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    // ==================== UPDATE ====================

    /**
     * Actualiza un producto existente.
     */
    public boolean actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, " +
                     "stock = ?, categoria = ?, activo = ?, fecha_actualizacion = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getCategoria());
            ps.setInt(6, producto.isActivo() ? 1 : 0);
            ps.setInt(7, producto.getId());

            return ps.executeUpdate() > 0;

        } finally {
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    // ==================== DELETE ====================

    /**
     * Elimina un producto por su ID.
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } finally {
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    // ==================== Obtener categorías únicas ====================

    public List<String> listarCategorias() throws SQLException {
        String sql = "SELECT DISTINCT categoria FROM productos WHERE categoria IS NOT NULL ORDER BY categoria";
        List<String> categorias = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }

            return categorias;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    // ==================== Validar usuario ====================

    /**
     * Valida credenciales de login.
     * @return nombre_completo si es válido, null si no
     */
    public String[] validarUsuario(String username, String password) throws SQLException {
        String sql = "SELECT nombre_completo, rol FROM usuarios WHERE username = ? AND password = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new String[]{ rs.getString("nombre_completo"), rs.getString("rol") };
            }

            return null;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConexionDB.cerrarConexion(conn);
        }
    }

    // ==================== Método auxiliar ====================

    /**
     * Mapea un ResultSet a un objeto Producto.
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setCategoria(rs.getString("categoria"));
        p.setActivo(rs.getInt("activo") == 1);

        Timestamp tsCreacion = rs.getTimestamp("fecha_creacion");
        if (tsCreacion != null) {
            p.setFechaCreacion(tsCreacion.toLocalDateTime());
        }

        Timestamp tsActualizacion = rs.getTimestamp("fecha_actualizacion");
        if (tsActualizacion != null) {
            p.setFechaActualizacion(tsActualizacion.toLocalDateTime());
        }

        return p;
    }
}
