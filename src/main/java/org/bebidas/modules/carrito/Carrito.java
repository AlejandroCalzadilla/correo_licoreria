package org.bebidas.modules.carrito;

import java.time.LocalDateTime;
import java.util.List;

import org.bebidas.modules.model.BaseEntity;
import org.bebidas.modules.usuarios.Usuario;

public class Carrito extends BaseEntity {
    private String sessionId;
    private Usuario usuario;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ItemCarrito> items;

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<ItemCarrito> getItems() { return items; }
    public void setItems(List<ItemCarrito> items) { this.items = items; }
}
