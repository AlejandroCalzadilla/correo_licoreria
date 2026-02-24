package org.bebidas.modules.clientes.services.interfaces;

import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.service.interfaces.GenericService;

public interface ClienteService extends GenericService<Cliente, Long> {
   
    void aprobarCredito(Long clienteId, double montoAprobado);
    void rechazarCredito(Long clienteId, String motivo);
    void actualizarEstadoVerificacion(Long clienteId, String estado, String observaciones, Long usuarioId);
}
