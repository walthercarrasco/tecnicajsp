package com.examen.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Clase Singleton para gestionar la conexión JDBC a Oracle Database 21c.
 *
 * TEMA: Conexión Java a Base de Datos
 *
 * Nota: En un proyecto real se usaría un DataSource o pool de conexiones
 * (HikariCP, C3P0, etc.), pero para la prueba técnica se usa DriverManager.
 *
 * Requisitos:
 *   - Oracle Database 21c (XE o Enterprise) instalado y corriendo en puerto 1521.
 *   - Usuario/schema 'EXAMEN' creado con permisos adecuados:
 *       CREATE USER examen IDENTIFIED BY examen123;
 *       GRANT CONNECT, RESOURCE, CREATE TABLE TO examen;
 *       ALTER USER examen QUOTA UNLIMITED ON USERS;
 */
public class ConexionDB {

    // ============================================================
    // CONFIGURACIÓN ORACLE 21c — Ajustar según tu entorno
    // ============================================================

    // Formato Service Name (Oracle 21c XE usa XEPDB1 como PDB por defecto)
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/testdb";

    // Si usas SID en lugar de Service Name, usa este formato:
    // private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";

    private static final String USUARIO = "walther";
    private static final String PASSWORD = "prueba";
    private static final String DRIVER   = "oracle.jdbc.OracleDriver";

    private static ConexionDB instancia;

    // Constructor privado (Singleton)
    private ConexionDB() {
        try {
            // Cargar el driver JDBC
            Class.forName(DRIVER);
            System.out.println("[ConexionDB] Driver Oracle Database 21c cargado correctamente.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver JDBC: " + DRIVER, e);
        }
    }

    /**
     * Obtener la instancia única (Patrón Singleton).
     */
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Obtener una conexión a la base de datos.
     * Cada llamada crea una nueva conexión — el llamador es responsable de cerrarla.
     */
    public Connection getConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);

        return conn;
    }
    /**
     * Cerrar una conexión de forma segura.
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("[ConexionDB] Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
