package com.mycompany.sistemadeventas.Logica;

import java.util.Date;

/**
 * PATRÓN FACTORY METHOD
 */

interface Creador<T> {
    T crear(Object... params);
}

class CreadorProducto implements Creador<Producto> {
    @Override
    public Producto crear(Object... params) {
        return new Producto(
            (String)  params[0],
            (String)  params[1],
            (Integer) params[2],
            (Double)  params[3]
        );
    }
}

class CreadorCliente implements Creador<Cliente> {
    // params: [0] rut, [1] nombre, [2] apellido, [3] correo, [4] numero
    @Override
    public Cliente crear(Object... params) {
        return new Cliente(
            (String) params[0],
            (String) params[1],
            (String) params[2],
            (String) params[3],
            (String) params[4]
        );
    }
}

class CreadorVenta implements Creador<ventaProducto> {
    // params: [0] producto, [1] cliente, [2] cantidad, [3] fecha
    @Override
    public ventaProducto crear(Object... params) {
        return new ventaProducto(
            (Producto) params[0],
            (Cliente)  params[1],
            (Integer)  params[2],
            (Date)     params[3]
        );
    }
}

public class EntidadFactory {

    public static Producto crearProducto(String nombre, String descripcion,
                                         int cantidad, double precio) {
        return new CreadorProducto().crear(nombre, descripcion, cantidad, precio);
    }

    public static Cliente crearCliente(String rut, String nombre, String apellido,
                                       String correo, String numero) {
        return new CreadorCliente().crear(rut, nombre, apellido, correo, numero);
    }

    public static ventaProducto crearVenta(Producto producto, Cliente cliente,
                                           int cantidad, Date fecha) {
        return new CreadorVenta().crear(producto, cliente, cantidad, fecha);
    }
}