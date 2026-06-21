package com.mycompany.sistemadeventas.Logica;

import com.mycompany.sistemadeventas.Data.ClienteDAO;
import com.mycompany.sistemadeventas.Data.ProductoDAO;
import com.mycompany.sistemadeventas.Data.VentaProductoDAO;
import com.mycompany.sistemadeventas.GUI.VentaSubject;
import java.util.Date;
import java.util.List;

/**
 * PATRÓN FACADE
 * Coordina DAOs, Factory y Observer en un solo método.
 */
public class VentaFacade {

    private final ProductoDAO      productoDAO;
    private final ClienteDAO       clienteDAO;
    private final VentaProductoDAO ventaDAO;
    private final VentaSubject     subject;

    public VentaFacade(VentaSubject subject) {
        this.productoDAO = new ProductoDAO();
        this.clienteDAO  = new ClienteDAO();
        this.ventaDAO    = new VentaProductoDAO();
        this.subject     = subject;
    }

    /**
     * Realiza el proceso completo de una venta.
     */
    public ventaProducto realizarVenta(String rutCliente, String nombreProducto,
                                       int cantidad, Date fecha) {
        // 1. Obtener entidades (por clave de negocio: rut y nombre)
        Cliente  cliente  = clienteDAO.buscarPorRut(rutCliente);
        Producto producto = productoDAO.buscarPorNombre(nombreProducto);

        if (cliente == null) {
            System.err.println("Cliente no encontrado: " + rutCliente);
            return null;
        }
        if (producto == null) {
            System.err.println("Producto no encontrado: " + nombreProducto);
            return null;
        }

        // 2. Validar stock
        if (producto.getCantidad() < cantidad) {
            System.err.println("Stock insuficiente. Disponible: " + producto.getCantidad());
            return null;
        }

        // 3. Crear venta con Factory Method
        ventaProducto venta = EntidadFactory.crearVenta(producto, cliente, cantidad, fecha);

        // 4. Guardar en BD
        ventaDAO.guardar(venta);

        // 5. Actualizar stock
        int nuevoStock = producto.getCantidad() - cantidad;
        producto.setCantidad(nuevoStock);
        productoDAO.actualizarStock(producto.getNombre(), nuevoStock);

        // 6. Notificar observadores (Observer)
        subject.notificar(venta);

        return venta;
    }

    public List<Producto> listarProductos() { return productoDAO.listarTodos(); }
    public List<Cliente>  listarClientes()  { return clienteDAO.listarTodos(); }
    public List<ventaProducto> listarVentas() { return ventaDAO.listarTodos(); }

    public void agregarProducto(String nombre, String descripcion, int cantidad, double precio) {
        productoDAO.guardar(EntidadFactory.crearProducto(nombre, descripcion, cantidad, precio));
    }

    public void agregarCliente(String rut, String nombre, String apellido,
                               String correo, String numero) {
        clienteDAO.guardar(EntidadFactory.crearCliente(rut, nombre, apellido, correo, numero));
    }

    public void eliminarCliente(String rut)     { clienteDAO.eliminarPorRut(rut); }
    public void eliminarProducto(String nombre) { productoDAO.eliminar(nombre); }
}