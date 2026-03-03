package org.bebidas.modules.service.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.modules.ventas.Venta;

public interface VentaService extends GenericService<Venta, Long> {
    
    List<Venta> buscarPorCliente(Long clienteId);
    List<Venta> buscarPorEstado(String estado);
   
  
    Venta completarVenta(Long ventaId);
    BigDecimal calcularTotalVentasPorCliente(Long clienteId);
    List<Venta> obtenerVentasPendientes();
    List<Venta> obtenerVentasCompletadas();
    List<Venta> obtenerVentasAnuladas();
    Venta crearVentaConDetalle(Long clienteId, String tipo, Long carritoId, String numeroCuotas, String metodoPago);
    Venta crearVentaBasica(Long clienteId, String tipo, String numeroCuotas, String metodoPago);
}
