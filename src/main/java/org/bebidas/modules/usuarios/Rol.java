package org.bebidas.modules.usuarios;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.usuarios.mappers.RolMapper;

public class Rol extends BaseEntity {
   
    private String nombre;
    private String descripcion;
    private boolean activo;

    // Getters and Setters
   

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    
    @Override
    public String toString() {
        return RolMapper.obtenerUnoTable(this);
    }

}
