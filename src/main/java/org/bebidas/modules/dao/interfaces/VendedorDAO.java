package org.bebidas.modules.dao.interfaces;

import java.util.List;

import org.bebidas.modules.model.Vendedor;

public interface VendedorDAO extends GenericDAO<Vendedor, Long> {
    List<Vendedor> buscarPorNombre(String nombre);
    Vendedor buscarPorUsuario(Long usuarioId);
    List<Vendedor> buscarPorCi(String ci);
}
