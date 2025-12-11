package org.bebidas.model;

public class Vendedor extends BaseEntity {
    private String ci;
    private String nombre;
    private Usuario usuario;

    // Getters and Setters
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
