package org.bebidas.modules.compras.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.compras.models.Compra;
import org.bebidas.modules.compras.repositories.interfaces.CompraDAO;
import org.bebidas.modules.compras.services.interfaces.ICompraService;
import java.time.LocalDate;
import java.util.List;

public class CompraServiceImpl extends GenericServiceImpl<Compra, Long> implements ICompraService {

    private final CompraDAO compraDAO;

    public CompraServiceImpl(CompraDAO compraDAO) {
        super(compraDAO);
        this.compraDAO = compraDAO;
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
        String nroCompra = generarSiguienteNroCompra();
        compra.setNroCompra(nroCompra);
        System.out.println("Número de compra generado: " + nroCompra);

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
    }

    private String generarSiguienteNroCompra() {

        List<Compra> compras = this.findAll();
        if (compras.isEmpty()) {
            return "C-000001";
        }
        int maxNumero = 0;
        for (Compra c : compras) {
            if (c.getNroCompra() != null && c.getNroCompra().startsWith("C-")) {
                try {
                    int numero = Integer.parseInt(c.getNroCompra().substring(2));
                    if (numero > maxNumero) {
                        maxNumero = numero;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar si no es válido
                }
            }
        }
        int siguiente = maxNumero + 1;
        return "C-" + String.format("%06d", siguiente);
        // En caso de error, usar un número por defecto
    }
}