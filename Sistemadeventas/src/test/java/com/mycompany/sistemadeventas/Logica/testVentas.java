package com.mycompany.sistemadeventas.Logica;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * PRUEBAS UNITARIAS - SISTEMA DE VENTAS
 *
 * Componentes probados:
 *  - Cliente        (creación, getters, toString)
 *  - Producto       (creación, stock, precio)
 *  - ventaProducto  (cálculo de total, referencias)
 *  - EntidadFactory (patrón Factory Method)
 *
 * Tipos de assert usados:
 *  - assertEquals   : verifica igualdad de valores
 *  - assertTrue     : verifica condiciones verdaderas
 *  - assertFalse    : verifica condiciones falsas
 *  - assertThrows   : verifica que se lanza la excepción esperada
 */
public class testVentas {

    // ── Objetos reutilizables entre pruebas ──────────────────────────────────
    private Cliente  clienteBase;
    private Producto productoBase;
    private Date     fechaBase;

    @Before
    public void setUp() {
        clienteBase  = new Cliente("12345678-9", "Juan", "Pérez", "juan@mail.com", "912345678");
        productoBase = new Producto("Laptop", "Laptop gaming", 10, 599990.0);
        fechaBase    = new Date();
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 1 — Cliente: los datos se almacenan correctamente al construir
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testClienteCreacionCorrecta() {
        assertEquals("El RUT debe coincidir",      "12345678-9",   clienteBase.getRut());
        assertEquals("El nombre debe coincidir",   "Juan",         clienteBase.getNombre());
        assertEquals("El apellido debe coincidir", "Pérez",        clienteBase.getApellido());
        assertEquals("El correo debe coincidir",   "juan@mail.com",clienteBase.getCorreo());
        assertEquals("El número debe coincidir",   "912345678",    clienteBase.getNumero());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 2 — Cliente: toString debe retornar "rut - nombre apellido"
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testClienteToStringFormato() {
        String resultado = clienteBase.toString();
        assertEquals("12345678-9 - Juan Pérez", resultado);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 3 — Cliente: setters actualizan los atributos
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testClienteSetters() {
        clienteBase.setNombre("Carlos");
        clienteBase.setCorreo("carlos@mail.com");

        assertEquals("Carlos",           clienteBase.getNombre());
        assertEquals("carlos@mail.com",  clienteBase.getCorreo());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 4 — Producto: precio y stock se guardan correctamente
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testProductoCreacionCorrecta() {
        assertEquals("Laptop",          productoBase.getNombre());
        assertEquals("Laptop gaming",   productoBase.getDescripcion());
        assertEquals(10,                productoBase.getCantidad());
        assertEquals(599990.0,          productoBase.getPrecio(), 0.001);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 5 — Producto: el stock no puede quedar negativo (regla de negocio)
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testProductoStockSuficiente() {
        int cantidadPedida = 5;
        assertTrue("Debe haber stock suficiente",
                productoBase.getCantidad() >= cantidadPedida);
    }

    @Test
    public void testProductoStockInsuficiente() {
        int cantidadPedida = 999;
        assertFalse("No debe haber stock para 999 unidades",
                productoBase.getCantidad() >= cantidadPedida);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 6 — Producto: precio mayor a cero (validación básica)
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testProductoPrecioPositivo() {
        assertTrue("El precio debe ser mayor a 0", productoBase.getPrecio() > 0);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 7 — ventaProducto: cálculo correcto del total (precio × cantidad)
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testVentaCalculoTotal() {
        ventaProducto venta = new ventaProducto(productoBase, clienteBase, 3, fechaBase);
        double esperado = 599990.0 * 3;          // 1_799_970.0
        assertEquals("El total debe ser precio × cantidad", esperado, venta.getTotal(), 0.001);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 8 — ventaProducto: total es 0 si el producto es null
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testVentaTotalSinProducto() {
        ventaProducto venta = new ventaProducto(null, clienteBase, 2, fechaBase);
        assertEquals("Total debe ser 0 si producto es null", 0.0, venta.getTotal(), 0.001);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 9 — ventaProducto: las referencias a cliente y producto son correctas
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testVentaReferenciasCorrectas() {
        ventaProducto venta = new ventaProducto(productoBase, clienteBase, 1, fechaBase);

        assertEquals("12345678-9", venta.getCliente().getRut());
        assertEquals("Laptop",     venta.getProducto().getNombre());
        assertEquals(1,            venta.getCantidad());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 10 — EntidadFactory: crearCliente produce un Cliente válido
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testFactoryCrearCliente() {
        Cliente c = EntidadFactory.crearCliente(
                "98765432-1", "Ana", "González", "ana@mail.com", "987654321");

        assertNotNull("El cliente no debe ser null", c);
        assertEquals("98765432-1",  c.getRut());
        assertEquals("Ana",         c.getNombre());
        assertEquals("González",    c.getApellido());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 11 — EntidadFactory: crearProducto produce un Producto válido
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testFactoryCrearProducto() {
        Producto p = EntidadFactory.crearProducto("Mouse", "Mouse inalámbrico", 50, 14990.0);

        assertNotNull("El producto no debe ser null", p);
        assertEquals("Mouse",               p.getNombre());
        assertEquals(50,                    p.getCantidad());
        assertEquals(14990.0,               p.getPrecio(), 0.001);
        assertTrue("Stock debe ser mayor a 0", p.getCantidad() > 0);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 12 — EntidadFactory: crearVenta produce una ventaProducto válida
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testFactoryCrearVenta() {
        ventaProducto v = EntidadFactory.crearVenta(productoBase, clienteBase, 2, fechaBase);

        assertNotNull("La venta no debe ser null", v);
        assertEquals(2,            v.getCantidad());
        assertNotNull("Fecha no debe ser null",     v.getFecha());
        assertEquals(productoBase, v.getProducto());
        assertEquals(clienteBase,  v.getCliente());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 13 — Producto: toString contiene nombre, precio y stock
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testProductoToStringContieneDatos() {
        String resultado = productoBase.toString();
        assertTrue("toString debe contener el nombre",  resultado.contains("Laptop"));
        assertTrue("toString debe contener el precio",  resultado.contains("599990"));
        assertTrue("toString debe contener el stock",   resultado.contains("10"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 14 — ventaProducto: actualizar cantidad recalcula el total
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testVentaCambioCantidadActualizaTotal() {
        ventaProducto venta = new ventaProducto(productoBase, clienteBase, 1, fechaBase);
        assertEquals(599990.0, venta.getTotal(), 0.001);

        venta.setCantidad(4);
        assertEquals(599990.0 * 4, venta.getTotal(), 0.001);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 15 — Cliente: RUT con formato inválido no debe ser igual a uno válido
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testClienteRutInvalidoDistintoAlValido() {
        Cliente invalido = new Cliente("00000000-0", "Test", "Test", "", "");
        assertFalse("RUTs distintos no deben ser iguales",
                clienteBase.getRut().equals(invalido.getRut()));
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 16 — assertThrows: ClassCastException si Factory recibe tipo incorrecto
    // ════════════════════════════════════════════════════════════════════════
    @Test
    public void testFactoryCrearProductoConTipoIncorrecto() {
        // CreadorProducto espera (String, String, Integer, Double).
        // Pasar un String donde va Integer debe lanzar ClassCastException.
        assertThrows(ClassCastException.class, () ->
                new CreadorProducto().crear("Producto", "Desc", "NO_ES_ENTERO", 100.0)
        );
    }
}