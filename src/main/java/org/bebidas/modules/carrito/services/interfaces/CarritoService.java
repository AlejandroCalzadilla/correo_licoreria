package org.bebidas.modules.carrito.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.service.interfaces.GenericService;

public interface CarritoService extends GenericService<Carrito, Long> {
    List<Carrito> buscarPorCliente(Long clienteId);

    Carrito buscarActivoPorCliente(Long clienteId);
    List<Carrito> buscarPorRangoFechas(String fechaInicio, String fechaFin);
    Carrito crearCarrito(Carrito carrito);
    void finalizarCarrito(Long carritoId);
    void cancelarCarrito(Long carritoId);
    BigDecimal calcularTotalCarrito(Long carritoId);
}
