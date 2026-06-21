package com.mycompany.sistemadeventas.Data;

import com.mycompany.sistemadeventas.Logica.Cliente;
import com.mycompany.sistemadeventas.Logica.Producto;
import com.mycompany.sistemadeventas.Logica.ventaProducto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN DAO
 * Maneja todas las operaciones de base de datos para ventaProducto.
 */
public class VentaProductoDAO {

    private Connection conn;

    public VentaProductoDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    public void guardar(ventaProducto v) {
        String sql = "INSERT INTO ventas (rut_cliente, nombre_producto, cantidad, fecha) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getCliente().getRut());
            ps.setString(2, v.getProducto().getNombre());
            ps.setInt(3, v.getCantidad());
            ps.setDate(4, new java.sql.Date(v.getFecha().getTime()));
            ps.executeUpdate();
            System.out.println("Venta registrada.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ventaProducto> listarTodos() {
        List<ventaProducto> lista = new ArrayList<>();
        String sql = "SELECT v.rut_cliente, v.nombre_producto, v.cantidad AS cantidad_vendida, v.fecha, " +
                     "c.nombre as cli_nombre, c.apellido, c.correo, c.numero, " +
                     "p.descripcion, p.cantidad AS stock_producto, p.precio " +
                     "FROM ventas v " +
                     "JOIN clientes c ON v.rut_cliente = c.rut " +
                     "JOIN productos p ON v.nombre_producto = p.nombre " +
                     "ORDER BY v.fecha DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM ventas WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ventaProducto mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(
            rs.getString("rut_cliente"),
            rs.getString("cli_nombre"),
            rs.getString("apellido"),
            rs.getString("correo"),
            rs.getString("numero")
        );
        Producto p = new Producto(
            rs.getString("nombre_producto"),
            rs.getString("descripcion"),
            rs.getInt("stock_producto"),
            rs.getDouble("precio")
        );
        int cantidadVendida = rs.getInt("cantidad_vendida");
        return new ventaProducto(p, c, cantidadVendida, rs.getDate("fecha"));
    }
}