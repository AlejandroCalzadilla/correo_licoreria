package org.bebidas.modules.compras.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.compras.Compra;
import org.bebidas.modules.compras.repositories.interfaces.CompraDAO;
import org.bebidas.modules.compras.services.interfaces.CompraService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class CompraServiceImpl extends GenericServiceImpl<Compra, Long> implements CompraService {

    private final CompraDAO compraDAO;

    public CompraServiceImpl(CompraDAO compraDAO) {
        super(compraDAO);
        this.compraDAO = compraDAO;
    }

    @Override
    public List<Compra> buscarPorProveedor(Long proveedorId) {
        return compraDAO.buscarPorProveedor(proveedorId);
    }

    

    @Override
    public List<Compra> buscarPorRangoFechas(Date fechaInicio, Date fechaFin) {
        return compraDAO.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    public List<Compra> buscarPorEstado(String estado) {
        return compraDAO.buscarPorEstado(estado);
    }

    @Override
    public Compra crearCompra(Compra compra) {
        // Validar datos de la compra
        if (compra.getProveedor() == null) {
            throw new IllegalArgumentException("La compra debe tener un proveedor");
        }
      
        // Configurar valores por defecto
        if (compra.getFecha() == null) {
            compra.setFecha(LocalDate.now());
        }
        if (compra.getEstado() == null) {
            compra.setEstado("PENDIENTE");
        }
        if (compra.getNroCompra() == null) {
            // Generar número de compra automático
            compra.setNroCompra("COMP-" + System.currentTimeMillis());
        }

        return save(compra);
    }

    @Override
    public void anularCompra(Long compraId, String motivo) {
        Compra compra = findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + compraId));

        // Validar que la compra se pueda anular
        if ("ANULADA".equals(compra.getEstado())) {
            throw new IllegalStateException("La compra ya está anulada");
        }
        if ("COMPLETADA".equals(compra.getEstado())) {
            throw new IllegalStateException("No se puede anular una compra completada");
        }

        // Cambiar estado a anulada
        compra.setEstado("ANULADA");
        save(compra);

        // Aquí podrías agregar lógica adicional como:
        // - Revertir movimientos de inventario
        // - Actualizar saldos de proveedores
        // - Registrar el motivo de anulación
    }

    @Override
    public void completarCompra(Long compraId) {
        Compra compra = findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + compraId));

        // Validar que la compra se pueda completar
        if (!"PENDIENTE".equals(compra.getEstado())) {
            throw new IllegalStateException("Solo se pueden completar compras en estado PENDIENTE");
        }

        // Cambiar estado a completada
        compra.setEstado("COMPLETADA");
        save(compra);

        // Aquí podrías agregar lógica adicional como:
        // - Actualizar inventario de productos
        // - Registrar movimientos de inventario
        // - Actualizar cuentas por pagar
    }

    @Override
    public BigDecimal calcularTotalComprasPorProveedor(Long proveedorId, Date fechaInicio, Date fechaFin) {
       
            
     return null;
    }

    @Override
    public List<Compra> obtenerComprasPendientes() {
        return buscarPorEstado("PENDIENTE");
    }

    @Override
    public List<Compra> obtenerComprasCompletadas() {
        return buscarPorEstado("COMPLETADA");
    }

    @Override
    public List<Compra> obtenerComprasAnuladas() {
        return buscarPorEstado("ANULADA");
    }
}