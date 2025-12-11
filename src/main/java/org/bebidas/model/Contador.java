package org.bebidas.model;

import java.time.LocalDateTime;

public class Contador extends BaseEntity {
    private String tipo;
    private String prefijo;
    private int valorActual;
    private int longitud;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getPrefijo() { return prefijo; }
    public void setPrefijo(String prefijo) { this.prefijo = prefijo; }
    
    public int getValorActual() { return valorActual; }
    public void setValorActual(int valorActual) { this.valorActual = valorActual; }
    
    public int getLongitud() { return longitud; }
    public void setLongitud(int longitud) { this.longitud = longitud; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Método para generar el siguiente número de secuencia
    public String getSiguienteNumero() {
        valorActual++;
        return String.format("%s%0" + longitud + "d", prefijo, valorActual);
    }
}
