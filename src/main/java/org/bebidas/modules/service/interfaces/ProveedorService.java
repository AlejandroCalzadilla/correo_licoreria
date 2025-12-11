package org.bebidas.modules.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.bebidas.modules.model.Proveedor;

public interface ProveedorService extends GenericService<Proveedor, Long> {
    List<Proveedor> buscarPorNombre(String nombre);
    Optional<Proveedor> buscarPorRuc(String ruc);

    List<Proveedor> buscarPorTipoProducto(String tipoProducto);
  
}
