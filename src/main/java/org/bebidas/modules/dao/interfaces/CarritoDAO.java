package org.bebidas.modules.dao.interfaces;

import java.util.List;

import org.bebidas.modules.carrito.Carrito;

public interface CarritoDAO extends GenericDAO<Carrito, Long> {
    List<Carrito> buscarPorCliente(Long clienteId);
    
    Carrito buscarActivoPorCliente(Long clienteId);
    List<Carrito> buscarPorRangoFechas(String fechaInicio, String fechaFin);
}
