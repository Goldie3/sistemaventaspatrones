package com.mycompany.sistemadeventas.Data;

import com.mycompany.sistemadeventas.Logica.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN DAO
 * Maneja todas las operaciones de base de datos para Producto.
 */
public class ProductoDAO {

    private Connection conn;

    public ProductoDAO() {
        // Usa el Singleton para obtener la conexión
        this.conn = ConexionDB.getInstance().getConexion();
    }

    public void guardar(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, cantidad, precio) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getCantidad());
            ps.setDouble(4, p.getPrecio());
            ps.executeUpdate();
            System.out.println("Producto guardado: " + p.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Producto buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET descripcion=?, cantidad=?, precio=? WHERE nombre=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getDescripcion());
            ps.setInt(2, p.getCantidad());
            ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarStock(String nombre, int nuevaCantidad) {
        String sql = "UPDATE productos SET cantidad=? WHERE nombre=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setString(2, nombre);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(String nombre) {
        String sql = "DELETE FROM productos WHERE nombre=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getInt("cantidad"),
            rs.getDouble("precio")
        );
    }
}
