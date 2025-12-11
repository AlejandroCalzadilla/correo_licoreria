package org.bebidas.dao.interfaces;

import org.bebidas.model.Vendedor;

import java.util.List;

public interface VendedorDAO extends GenericDAO<Vendedor, Long> {
    List<Vendedor> buscarPorNombre(String nombre);
    Vendedor buscarPorUsuario(Long usuarioId);
    List<Vendedor> buscarPorCi(String ci);
}
