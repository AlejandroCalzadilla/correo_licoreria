package org.bebidas.modules.categorias;

import org.bebidas.core.util.BaseEntity;

public class Categoria extends BaseEntity {
    private String nombre;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
