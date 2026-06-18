package org.bebidas.modules.compras.services.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.bebidas.modules.compras.models.Compra;
import org.bebidas.modules.service.interfaces.GenericService;

public interface ICompraService extends GenericService<Compra, Long> {

    // Métodos CRUD específicos
    Compra crearCompra(Compra compra);

    void anularCompra(Long compraId, String motivo);

    void completarCompra(Long compraId);

}