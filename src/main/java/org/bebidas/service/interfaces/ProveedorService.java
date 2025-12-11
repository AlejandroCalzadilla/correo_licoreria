package org.bebidas.service.interfaces;

import org.bebidas.model.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService extends GenericService<Proveedor, Long> {
    List<Proveedor> buscarPorNombre(String nombre);
    Optional<Proveedor> buscarPorRuc(String ruc);

    List<Proveedor> buscarPorTipoProducto(String tipoProducto);
  
}
