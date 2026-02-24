package org.bebidas.modules.proveedor.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.proveedor.Proveedor;
import org.bebidas.modules.proveedor.repositories.ProveedorDAO;
import org.bebidas.modules.service.interfaces.ProveedorService;

import java.util.List;
import java.util.Optional;


public class ProveedorServiceImpl extends GenericServiceImpl<Proveedor, Long> implements ProveedorService {

    private final ProveedorDAO proveedorDAO;

    public ProveedorServiceImpl(ProveedorDAO proveedorDAO) {
        super(proveedorDAO);
        this.proveedorDAO = proveedorDAO;
    }

    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorDAO.buscarPorNombre(nombre);
    }

    @Override
    public Optional<Proveedor> buscarPorRuc(String ruc) {
        List<Proveedor> proveedores = proveedorDAO.buscarPorRuc(ruc);
        return proveedores.isEmpty() ? Optional.empty() : Optional.of(proveedores.get(0));
    }

   
    @Override
    
    public List<Proveedor> buscarPorTipoProducto(String tipoProducto) {
        return proveedorDAO.buscarPorTipoProducto(tipoProducto);
    }

   
    

    @Override
    public void delete(Long id) {
        Proveedor proveedor = findById(id).orElseThrow(
            () -> new RuntimeException("Proveedor no encontrado con ID: " + id)
        );
        
      /*   if (!proveedor.getProductos().isEmpty()) {
            throw new IllegalStateException(
                "No se puede eliminar el proveedor porque tiene productos asociados"
            );
        } */
        
        super.delete(id);
    }
}
