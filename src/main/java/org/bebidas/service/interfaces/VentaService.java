package org.bebidas.service.interfaces;

import org.bebidas.model.Venta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VentaService extends GenericService<Venta, Long> {
    List<Venta> buscarPorFecha(LocalDate fecha);
    List<Venta> buscarPorCliente(Long clienteId);
    List<Venta> buscarPorEstado(String estado);
    List<Venta> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Venta> buscarPorUsuario(Long usuarioId);
    Venta crearVenta(Venta venta);
    void anularVenta(Long ventaId, String motivo);
    Venta completarVenta(Long ventaId);
    BigDecimal calcularTotalVentasPorCliente(Long clienteId);
    List<Venta> obtenerVentasPendientes();
    List<Venta> obtenerVentasCompletadas();
    List<Venta> obtenerVentasAnuladas();
}
