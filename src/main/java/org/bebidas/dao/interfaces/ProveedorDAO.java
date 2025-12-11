package org.bebidas.dao.interfaces;

import org.bebidas.model.Proveedor;

import java.util.List;

public interface ProveedorDAO extends GenericDAO<Proveedor, Long> {
    List<Proveedor> buscarPorNombre(String nombre);
    List<Proveedor> buscarPorRuc(String ruc);

    List<Proveedor> buscarPorTipoProducto(String tipoProducto);
}
