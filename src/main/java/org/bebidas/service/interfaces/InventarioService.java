package org.bebidas.service.interfaces;

import org.bebidas.model.Inventario;

import java.time.LocalDate;
import java.util.List;

public interface InventarioService extends GenericService<Inventario, Long> {
    List<Inventario> buscarPorProducto(Long productoId);
    List<Inventario> buscarPorTipoMovimiento(String tipoMovimiento);
    List<Inventario> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Inventario> buscarPorUsuario(Long usuarioId);
    Integer obtenerStockActual(Long productoId);
    void registrarEntrada(Inventario movimiento);
    void registrarSalida(Inventario movimiento);
    void ajustarInventario(Inventario movimiento, String motivo);
}
