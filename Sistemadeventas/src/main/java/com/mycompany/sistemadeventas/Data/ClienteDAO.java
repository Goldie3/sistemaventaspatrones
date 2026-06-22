package com.mycompany.sistemadeventas.Data;

import com.mycompany.sistemadeventas.Logica.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN DAO Maneja todas las operaciones de base de datos para Cliente.
 */
public class ClienteDAO {

    private Connection conn;

    public ClienteDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    public void guardar(Cliente c) {
        String sql = "INSERT INTO clientes (rut, nombre, apellido, correo, numero) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getRut());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getNumero());
            ps.executeUpdate();

            // Recuperar el id generado por la BD
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                c.setId(generatedKeys.getInt(1));
            }
            System.out.println("Cliente guardado con id: " + c.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente buscarPorId(Integer id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cliente buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM clientes WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Busca un cliente por su rut, que es la clave de negocio usada para
     * identificar clientes en ventas (a diferencia del id autoincremental).
     */
    public Cliente buscarPorRut(String rut) {
        String sql = "SELECT * FROM clientes WHERE rut = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rut);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizar(Cliente c) {
        String sql = "UPDATE clientes SET nombre=?, apellido=?, correo=?, numero=? WHERE rut=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getNumero());
            ps.setString(5, c.getRut());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(Integer id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Variante de eliminar() usando el rut en lugar del id. VentaFacade trabaja
     * con ruts (clave de negocio), no con ids autoincrementales.
     */
    public void eliminarPorRut(String rut) {
        String sql = "DELETE FROM clientes WHERE rut=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rut);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(
                rs.getString("rut"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("correo"),
                rs.getString("numero")
        );
        c.setId(rs.getInt("id"));
        return c;
    }
}
