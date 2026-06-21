/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadeventas.Logica;

import java.util.Date;

/**
 *
 * @author bruno
 */
public class ventaProducto {
    Producto producto;
    Cliente cliente;
    private int cantidad;
    private Date fecha;

    public ventaProducto(Producto producto, Cliente cliente, int cantidad, Date fecha) {
        this.producto = producto;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return producto != null ? producto.getPrecio() * cantidad : 0.0;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}