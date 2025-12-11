package org.bebidas.modules.carrito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.inventario.Producto;

public class ItemCarrito extends BaseEntity {
    private Carrito carrito;
    private Producto producto;
    private int cantidad;
    private BigDecimal precio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public BigDecimal getSubtotal() {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
