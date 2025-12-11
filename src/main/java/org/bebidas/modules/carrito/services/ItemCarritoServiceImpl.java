package org.bebidas.modules.carrito.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.carrito.services.interfaces.CarritoService;
import org.bebidas.modules.carrito.services.interfaces.ItemCarritoService;
import org.bebidas.modules.dao.interfaces.ItemCarritoDAO;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.InventarioServiceImpl;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.service.interfaces.ProductoService;

import java.math.BigDecimal;
import java.util.List;


public class ItemCarritoServiceImpl extends GenericServiceImpl<ItemCarrito, Long> implements ItemCarritoService {

    private final ItemCarritoDAO itemCarritoDAO;
    private final CarritoService carritoService;
    private final ProductoService productoService;
    private final InventarioServiceImpl inventarioService;

    public ItemCarritoServiceImpl(ItemCarritoDAO itemCarritoDAO, 
                                CarritoService carritoService,
                                ProductoService productoService,
                                InventarioServiceImpl inventarioService) {
        super(itemCarritoDAO);
        this.itemCarritoDAO = itemCarritoDAO;
        this.carritoService = carritoService;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }

    public List<ItemCarrito> buscarPorCarrito(Long carritoId) {
        return itemCarritoDAO.buscarPorCarrito(carritoId);
    }

    @Override
    public List<ItemCarrito> buscarPorProducto(Long productoId) {
        return itemCarritoDAO.buscarPorProducto(productoId);
    }



    public ItemCarrito agregarItem(ItemCarrito item) {
        // Validar que el carrito existe y está activo
        Carrito carrito = carritoService.findById(item.getCarrito().getId())
            .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
        
      
        
        // Validar que el producto existe y tiene stock disponible
        Producto producto = productoService.findById(item.getProducto().getId())
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        Integer productostock= inventarioService.obtenerStockActual(item.getProducto().getId());
        
            
        if (productostock < item.getCantidad()) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        
        // Verificar si el producto ya está en el carrito
        List<ItemCarrito> itemsExistentes = itemCarritoDAO.buscarPorCarrito(carrito.getId());
        for (ItemCarrito existente : itemsExistentes) {
            if (existente.getProducto().getId().equals(producto.getId())) {
                // Si el producto ya está en el carrito, actualizar la cantidad
                int nuevaCantidad = existente.getCantidad() + item.getCantidad();
                actualizarCantidad(existente.getId(), nuevaCantidad);
                return existente;
            }
        }
        
        // Si el producto no está en el carrito, crear un nuevo ítem
       // item.setPrecio(producto.getPrecioVenta());
        //item.setSubtotal(producto.getPrecioVenta().multiply(BigDecimal.valueOf(item.getCantidad())));
        
        ItemCarrito itemGuardado = save(item);
        actualizarTotalCarrito(carrito.getId());
        
        return itemGuardado;
    }

    @Override
    
    public void actualizarCantidad(Long itemId, int nuevaCantidad) {
        ItemCarrito item = findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Ítem no encontrado"));
        
        // Validar que el carrito está activo
        Carrito carrito = carritoService.findById(item.getCarrito().getId())
            .orElseThrow(() -> new IllegalStateException("Carrito no encontrado"));
            
        
        
        // Validar stock
        if (nuevaCantidad <= 0) {
            eliminarItem(itemId);
            return;
        }
        
        Producto producto = item.getProducto();
        Integer productostock= inventarioService.obtenerStockActual(producto.getId());
        if (productostock < nuevaCantidad) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        
        // Actualizar cantidad y subtotal
        item.setCantidad(nuevaCantidad);
        item.setPrecio(item.getPrecio().multiply(BigDecimal.valueOf(nuevaCantidad)));
        save(item);
        
        actualizarTotalCarrito(carrito.getId());
    }

    @Override
   
    public void eliminarItem(Long itemId) {
        ItemCarrito item = findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Ítem no encontrado"));
        
        Long carritoId = item.getCarrito().getId();
        delete(itemId);
        actualizarTotalCarrito(carritoId);
    }

    @Override
    
    public void vaciarCarrito(Long carritoId) {
        Carrito carrito = carritoService.findById(carritoId)
            .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
            
        itemCarritoDAO.eliminarPorCarrito(carritoId);
        actualizarTotalCarrito(carritoId);
    }

    @Override

    public BigDecimal calcularSubtotal(Long itemId) {
        ItemCarrito item = findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Ítem no encontrado"));
            
        return item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
    }
    
    private void actualizarTotalCarrito(Long carritoId) {
        List<ItemCarrito> items = buscarPorCarrito(carritoId);
        BigDecimal total = items.stream()
            .map(ItemCarrito::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        Carrito carrito = carritoService.findById(carritoId)
            .orElseThrow(() -> new IllegalStateException("Carrito no encontrado"));
            
       // carrito.setTotal(total);
        carritoService.save(carrito);
    }
}
