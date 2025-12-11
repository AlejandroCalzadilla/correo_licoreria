package org.bebidas.modules.inventario;

import java.time.LocalDate;

import org.bebidas.modules.compras.DetalleCompra;
import org.bebidas.modules.model.BaseEntity;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.ventas.DetalleVenta;

public class Inventario extends BaseEntity {
    private String tipoMovimiento;
    private int cantidad;
    private LocalDate fecha;
    private int stockActual;
    private String glosa;
    private Usuario usuario;
    private DetalleCompra detalleCompra;
    private Producto producto;
    private DetalleVenta detalleVenta;

    // Getters and Setters
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    
    public String getGlosa() { return glosa; }
    public void setGlosa(String glosa) { this.glosa = glosa; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public DetalleCompra getDetalleCompra() { return detalleCompra; }
    public void setDetalleCompra(DetalleCompra detalleCompra) { this.detalleCompra = detalleCompra; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public DetalleVenta getDetalleVenta() { return detalleVenta; }
    public void setDetalleVenta(DetalleVenta detalleVenta) { this.detalleVenta = detalleVenta; }
}
