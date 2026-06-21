package com.mycompany.sistemadeventas.GUI;

import com.mycompany.sistemadeventas.Logica.ventaProducto;

/**
 * PATRÓN OBSERVER
 * Cualquier ventana que necesite reaccionar cuando se registra una venta
 * (por ejemplo, refrescar su tabla o el stock mostrado) implementa esta
 * interfaz y se suscribe a un VentaSubject.
 */
public interface VentaObserver {
    void onNuevaVenta(ventaProducto venta);
}
