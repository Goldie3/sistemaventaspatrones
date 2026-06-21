package com.mycompany.sistemadeventas.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PATRÓN SINGLETON
 * Garantiza una única instancia de la conexión a la base de datos.
 */
public class ConexionDB {

    private static ConexionDB instancia;
    private Connection conexion;

    private static final String URL      = "jdbc:mysql://localhost:3306/ventas_sistema123";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "1234";

    // Constructor privado: nadie puede hacer new ConexionDB()
    private ConexionDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión establecida.");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error al conectar: " + e.getMessage());
        }
    }

    // Punto de acceso global
    public static ConexionDB getInstance() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                instancia = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
