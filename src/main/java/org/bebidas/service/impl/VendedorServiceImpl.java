package org.bebidas.service.impl;

import org.bebidas.dao.interfaces.VendedorDAO;
import org.bebidas.model.Vendedor;
import org.bebidas.service.interfaces.VendedorService;

import java.util.List;

public class VendedorServiceImpl extends GenericServiceImpl<Vendedor, Long> implements VendedorService {

    private final VendedorDAO vendedorDAO;

    public VendedorServiceImpl(VendedorDAO vendedorDAO) {
        super(vendedorDAO);
        this.vendedorDAO = vendedorDAO;
    }

    @Override
    public List<Vendedor> buscarPorNombre(String nombre) {
        return vendedorDAO.buscarPorNombre(nombre);
    }

    @Override
    public Vendedor buscarPorUsuario(Long usuarioId) {
        return vendedorDAO.buscarPorUsuario(usuarioId);
    }

    @Override
    public List<Vendedor> buscarPorCi(String ci) {
        return vendedorDAO.buscarPorCi(ci);
    }
}