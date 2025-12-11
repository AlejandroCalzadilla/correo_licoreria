package org.bebidas.service.interfaces;

import org.bebidas.model.Vendedor;

import java.util.List;

public interface VendedorService extends GenericService<Vendedor, Long> {
    List<Vendedor> buscarPorNombre(String nombre);
    Vendedor buscarPorUsuario(Long usuarioId);
    List<Vendedor> buscarPorCi(String ci);
}