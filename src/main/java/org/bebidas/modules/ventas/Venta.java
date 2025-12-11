package org.bebidas.modules.ventas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.usuarios.Usuario;

public class Venta extends BaseEntity {
    private String nroVenta;
    private LocalDate fecha;
    private String tipo;
    private BigDecimal montoTotal;
    private BigDecimal saldo;
    private String numeroCuotas;
    private String estado;
    private Cliente cliente;
    private String metodoPago;
    private String estadoPago;
    private Usuario usuario;
    private List<DetalleVenta> detalles;

    // Getters and Setters
    public String getNroVenta() { return nroVenta; }
    public void setNroVenta(String nroVenta) { this.nroVenta = nroVenta; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    
    public String getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(String numeroCuotas) { this.numeroCuotas = numeroCuotas; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}
