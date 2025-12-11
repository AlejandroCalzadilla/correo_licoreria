package org.bebidas.modules.clientes;

import java.time.LocalDateTime;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.usuarios.Usuario;

public class Cliente extends BaseEntity {
    private String ci;
    private String nombre;
    private String telefono;
    private String direccion;
    private char estado;
    private Usuario usuario;
    private boolean creditoAprobado;
    private double limiteCredito;
    private String carnetAnverso;
    private String carnetReverso;
    private String fotoLuz;
    private String fotoAgua;
    private String fotoGarantia;
    private String estadoVerificacion;
    private String observacionesVerificacion;
    private LocalDateTime fechaVerificacion;
    private Usuario verificadoPor;

    // Getters and Setters
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public char getEstado() { return estado; }
    public void setEstado(char estado) { this.estado = estado; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public boolean isCreditoAprobado() { return creditoAprobado; }
    public void setCreditoAprobado(boolean creditoAprobado) { this.creditoAprobado = creditoAprobado; }
    
    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { this.limiteCredito = limiteCredito; }
    
    public String getCarnetAnverso() { return carnetAnverso; }
    public void setCarnetAnverso(String carnetAnverso) { this.carnetAnverso = carnetAnverso; }
    
    public String getCarnetReverso() { return carnetReverso; }
    public void setCarnetReverso(String carnetReverso) { this.carnetReverso = carnetReverso; }
    
    public String getFotoLuz() { return fotoLuz; }
    public void setFotoLuz(String fotoLuz) { this.fotoLuz = fotoLuz; }
    
    public String getFotoAgua() { return fotoAgua; }
    public void setFotoAgua(String fotoAgua) { this.fotoAgua = fotoAgua; }
    
    public String getFotoGarantia() { return fotoGarantia; }
    public void setFotoGarantia(String fotoGarantia) { this.fotoGarantia = fotoGarantia; }
    
    public String getEstadoVerificacion() { return estadoVerificacion; }
    public void setEstadoVerificacion(String estadoVerificacion) { this.estadoVerificacion = estadoVerificacion; }
    
    public String getObservacionesVerificacion() { return observacionesVerificacion; }
    public void setObservacionesVerificacion(String observacionesVerificacion) { this.observacionesVerificacion = observacionesVerificacion; }
    
    public LocalDateTime getFechaVerificacion() { return fechaVerificacion; }
    public void setFechaVerificacion(LocalDateTime fechaVerificacion) { this.fechaVerificacion = fechaVerificacion; }
    
    public Usuario getVerificadoPor() { return verificadoPor; }
    public void setVerificadoPor(Usuario verificadoPor) { this.verificadoPor = verificadoPor; }
}
