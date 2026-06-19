package org.bebidas.modules.carrito.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.repositories.interfaces.CarritoDAO;
import org.bebidas.modules.carrito.repositories.interfaces.ItemCarritoDAO;
import org.bebidas.modules.carrito.services.interfaces.CarritoService;
import org.bebidas.modules.clientes.services.interfaces.ClienteService;
import org.bebidas.modules.usuarios.services.interfaces.UsuarioService;

import java.math.BigDecimal;
import java.util.List;

public class CarritoServiceImpl extends GenericServiceImpl<Carrito, Long> implements CarritoService {

    private final CarritoDAO carritoDAO;
    private final ItemCarritoDAO itemCarritoDAO;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    public CarritoServiceImpl(CarritoDAO carritoDAO, ItemCarritoDAO itemCarritoDAO,
            ClienteService clienteService, UsuarioService usuarioService) {
        super(carritoDAO);
        this.carritoDAO = carritoDAO;
        this.itemCarritoDAO = itemCarritoDAO;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<Carrito> buscarPorCliente(Long clienteId) {
        return carritoDAO.buscarPorCliente(clienteId);
    }

    @Override
    public Carrito buscarActivoPorCliente(Long clienteId) {
        return carritoDAO.buscarActivoPorCliente(clienteId);
    }

    @Override
    public List<Carrito> buscarPorRangoFechas(String fechaInicio, String fechaFin) {
        return carritoDAO.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    public Carrito crearCarrito(Carrito carrito) {

        return save(carrito);
    }

    @Override
    public void finalizarCarrito(Long carritoId) {
        Carrito carrito = findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        // Validar que el carrito tenga items
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("No se puede finalizar un carrito vacío");
        }

        // Actualizar el estado del carrito
        // carrito.setEstado("FINALIZADO");
        // carrito.setFechaModificacion(LocalDateTime.now());
        save(carrito);

        // Aquí podrías agregar lógica adicional como crear una venta a partir del
        // carrito
    }

    @Override
    public void cancelarCarrito(Long carritoId) {
        Carrito carrito = findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        // Actualizar el estado del carrito
        // carrito.setEstado("CANCELADO");
        // carrito.setFechaModificacion(LocalDateTime.now());
        save(carrito);
    }

    @Override
    public BigDecimal calcularTotalCarrito(Long carritoId) {
        /*
         * List<ItemCarrito> items = itemCarritoDAO.buscarPorCarrito(carritoId);
         * return items.stream()
         * .map(item ->
         * item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
         * .reduce(BigDecimal.ZERO, BigDecimal::add);
         */

        return null;
    }
}
