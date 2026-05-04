package org.bebidas.modules.compras.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.compras.DetalleCompra;
import org.bebidas.modules.compras.repositories.interfaces.DetalleCompraDAO;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.InventarioServiceImpl;
import org.bebidas.modules.service.interfaces.DetalleCompraService;
import org.bebidas.modules.service.interfaces.InventarioService;

import java.util.List;

public class DetalleCompraServiceImpl extends GenericServiceImpl<DetalleCompra, Long> implements DetalleCompraService {

    private final DetalleCompraDAO detalleCompraDAO;
    private final InventarioServiceImpl inventarioService;

    public DetalleCompraServiceImpl(DetalleCompraDAO detalleCompraDAO, InventarioService inventarioService) {
        super(detalleCompraDAO);
        this.detalleCompraDAO = detalleCompraDAO;
        this.inventarioService = (InventarioServiceImpl)  inventarioService;
    }

    @Override
    public List<DetalleCompra> buscarPorCompra(Long compraId) {
        return detalleCompraDAO.buscarPorCompra(compraId);
    }

    @Override
    public List<DetalleCompra> buscarPorProducto(Long productoId) {
        return detalleCompraDAO.buscarPorProducto(productoId);
    }

    @Override
    public DetalleCompra insertar(DetalleCompra detalle) {

        Inventario inventario = new Inventario();
        inventario.setProducto(detalle.getProducto());
        inventario.setCantidad(detalle.getCantidad());

        DetalleCompra detalleInsertado = detalleCompraDAO.insertar(detalle);
        inventarioService.registrarEntradaCompra(inventario,detalleInsertado);
        return detalleInsertado;
    }

    @Override
    public DetalleCompra actualizar(DetalleCompra detalle) {
        DetalleCompra detalleActual = this.findById(detalle.getId())
            .orElseThrow(() -> new IllegalArgumentException("El detalle de compra con ID " + detalle.getId() + " no existe"));
        Integer stockActual = inventarioService.obtenerStockActual(detalle.getProducto().getId());
        Inventario inventario = new Inventario();
        inventario.setProducto(detalle.getProducto());
        inventario.setCantidad(detalle.getCantidad());
        inventario.setCantidad(1);//se le pasa por defecto 1 porque su servicio lo requiere, pero no se usara
        Integer difference = detalle.getCantidad() - detalleActual.getCantidad();
        inventario.setStockActual(stockActual + difference);
        inventarioService.ajustarInventario(inventario, "ACTUALIZACION DE DETALLE DE COMPRA CON ID: " + detalle.getId());
        return detalleCompraDAO.actualizar(detalle);
    }

    @Override
    public void eliminar(Long id) {
        DetalleCompra detalleActual = this.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("El detalle de compra con ID " + id + " no existe"));
        Integer stockActual = inventarioService.obtenerStockActual(detalleActual.getProducto().getId());
        Inventario inventario = new Inventario();
        inventario.setProducto(detalleActual.getProducto());
        inventario.setCantidad(detalleActual.getCantidad());
        if( stockActual < detalleActual.getCantidad() ){
            throw new IllegalArgumentException("No se puede eliminar el detalle de compra con ID " + id + " porque no hay suficiente stock en inventario");
        }
        inventario.setStockActual(stockActual - detalleActual.getCantidad());
        inventarioService.ajustarInventario(inventario, "ELIMINACION DE DETALLE DE COMPRA CON ID: " + id);
        detalleCompraDAO.eliminar(id);
    }
}