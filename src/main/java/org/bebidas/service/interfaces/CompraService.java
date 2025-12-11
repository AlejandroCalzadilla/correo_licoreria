package org.bebidas.service.interfaces;

import org.bebidas.model.Compra;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CompraService extends GenericService<Compra, Long> {
    List<Compra> buscarPorProveedor(Long proveedorId);
  
    List<Compra> buscarPorRangoFechas(Date fechaInicio, Date fechaFin);
    List<Compra> buscarPorEstado(String estado);

    // Métodos CRUD específicos
    Compra crearCompra(Compra compra);
    void anularCompra(Long compraId, String motivo);
    void completarCompra(Long compraId);
    BigDecimal calcularTotalComprasPorProveedor(Long proveedorId, Date fechaInicio, Date fechaFin);
    List<Compra> obtenerComprasPendientes();
    List<Compra> obtenerComprasCompletadas();
    List<Compra> obtenerComprasAnuladas();
}