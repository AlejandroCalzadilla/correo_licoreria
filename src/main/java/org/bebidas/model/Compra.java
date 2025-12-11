package org.bebidas.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Compra extends BaseEntity {
    private String nroCompra;
    private LocalDate fecha;
    private String estado;
    private Proveedor proveedor;
    private String descripcion;
    private List<DetalleCompra> detalles;

    // Getters and Setters
    public String getNroCompra() { return nroCompra; }
    public void setNroCompra(String nroCompra) { this.nroCompra = nroCompra; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
  
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    
    
    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }



    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}