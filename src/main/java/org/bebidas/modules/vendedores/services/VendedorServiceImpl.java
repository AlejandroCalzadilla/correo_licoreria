package org.bebidas.modules.vendedores.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.service.interfaces.VendedorService;
import org.bebidas.modules.vendedores.Vendedor;
import org.bebidas.modules.vendedores.repositories.VendedorDAO;

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