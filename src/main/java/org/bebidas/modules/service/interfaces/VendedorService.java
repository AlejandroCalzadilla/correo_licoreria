package org.bebidas.modules.service.interfaces;

import java.util.List;

import org.bebidas.modules.vendedores.Vendedor;

public interface VendedorService extends GenericService<Vendedor, Long> {
    List<Vendedor> buscarPorNombre(String nombre);
    Vendedor buscarPorUsuario(Long usuarioId);
    List<Vendedor> buscarPorCi(String ci);
}