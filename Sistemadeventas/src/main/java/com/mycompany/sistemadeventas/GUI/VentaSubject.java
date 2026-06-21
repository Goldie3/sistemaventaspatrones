package com.mycompany.sistemadeventas.GUI;

import com.mycompany.sistemadeventas.Logica.ventaProducto;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN OBSERVER
 * Notifica a todas las ventanas suscritas cada vez que VentaFacade
 * registra una nueva venta (por ejemplo, para refrescar tablas o stock
 * mostrado en otras pantallas abiertas al mismo tiempo).
 *
 * Se implementa como instancia única (mismo enfoque que ConexionDB) para
 * que cualquier ventana pueda suscribirse con VentaSubject.getInstance()
 * sin que Principal tenga que repartir la referencia manualmente.
 */
public class VentaSubject {

    private static final VentaSubject instancia = new VentaSubject();
    private final List<VentaObserver> observadores = new ArrayList<>();

    private VentaSubject() {
    }

    public static VentaSubject getInstance() {
        return instancia;
    }

    public void agregarObservador(VentaObserver obs) {
        if (!observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    public void quitarObservador(VentaObserver obs) {
        observadores.remove(obs);
    }

    public void notificar(ventaProducto venta) {
        for (VentaObserver obs : observadores) {
            obs.onNuevaVenta(venta);
        }
    }
}
