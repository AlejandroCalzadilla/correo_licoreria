package org.bebidas.modules.creditos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.pagos.Pago;
import org.bebidas.modules.ventas.Venta;

public class Credito extends BaseEntity {
    private Venta venta;
    private BigDecimal montoTotal;
    private BigDecimal saldo;
    private String numeroCuotas;
    private LocalDate fechaInicio;
    private String estado;
    private List<Pago> pagos;

    // Getters and Setters
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(String numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
