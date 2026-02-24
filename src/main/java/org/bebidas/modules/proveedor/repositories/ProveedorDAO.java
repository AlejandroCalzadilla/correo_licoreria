package org.bebidas.modules.proveedor.repositories;

import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.proveedor.Proveedor;

public interface ProveedorDAO extends GenericDAO<Proveedor, Long> {
    List<Proveedor> buscarPorNombre(String nombre);
    List<Proveedor> buscarPorRuc(String ruc);

    List<Proveedor> buscarPorTipoProducto(String tipoProducto);
}
