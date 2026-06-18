package org.bebidas.modules.clientes;

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
    private Usuario verificadoPor;

    // Getters and Setters
    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isCreditoAprobado() {
        return creditoAprobado;
    }

    public void setCreditoAprobado(boolean creditoAprobado) {
        this.creditoAprobado = creditoAprobado;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public Usuario getVerificadoPor() {
        return verificadoPor;
    }

    public void setVerificadoPor(Usuario verificadoPor) {
        this.verificadoPor = verificadoPor;
    }
}
