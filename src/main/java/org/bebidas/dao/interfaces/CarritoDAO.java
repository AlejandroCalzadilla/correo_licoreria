package org.bebidas.dao.interfaces;

import org.bebidas.model.Carrito;

import java.util.List;

public interface CarritoDAO extends GenericDAO<Carrito, Long> {
    List<Carrito> buscarPorCliente(Long clienteId);
    
    Carrito buscarActivoPorCliente(Long clienteId);
    List<Carrito> buscarPorRangoFechas(String fechaInicio, String fechaFin);
}
