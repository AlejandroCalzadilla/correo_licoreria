package org.bebidas.service.interfaces;

import org.bebidas.model.Carrito;

import java.math.BigDecimal;
import java.util.List;

public interface CarritoService extends GenericService<Carrito, Long> {
    List<Carrito> buscarPorCliente(Long clienteId);

    Carrito buscarActivoPorCliente(Long clienteId);
    List<Carrito> buscarPorRangoFechas(String fechaInicio, String fechaFin);
    Carrito crearCarrito(Carrito carrito);
    void finalizarCarrito(Long carritoId);
    void cancelarCarrito(Long carritoId);
    BigDecimal calcularTotalCarrito(Long carritoId);
}
