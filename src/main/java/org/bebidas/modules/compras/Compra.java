package org.bebidas.modules.compras;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.proveedor.Proveedor;

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

    public Long getProveedorId() {
        return proveedor != null ? proveedor.getId() : null;
    }
    
    
    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }



    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + getId() +
                ", nroCompra='" + nroCompra + '\'' +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", proveedor=" + (proveedor != null ? proveedor.getNombre() : "null") +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}