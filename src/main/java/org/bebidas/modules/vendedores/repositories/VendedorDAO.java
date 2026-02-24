package org.bebidas.modules.vendedores.repositories;

import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.vendedores.Vendedor;

public interface VendedorDAO extends GenericDAO<Vendedor, Long> {
    List<Vendedor> buscarPorNombre(String nombre);
    Vendedor buscarPorUsuario(Long usuarioId);
    List<Vendedor> buscarPorCi(String ci);
}
