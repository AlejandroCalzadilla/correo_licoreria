package org.bebidas.modules.compras;

import java.math.BigDecimal;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.inventario.Producto;

public class DetalleCompra extends BaseEntity {
    private Compra compra;
    private Producto producto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    // Getters and Setters
    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad;
        calcularSubtotal();
    }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { 
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    private void calcularSubtotal() {
        if (precioUnitario != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
}