package org.bebidas.modules.service.interfaces;

import java.util.List;

import org.bebidas.modules.clientes.Cliente;

public interface ClienteService extends GenericService<Cliente, Long> {
    List<Cliente> buscarPorNombre(String nombre);
    List<Cliente> buscarPorCi(String ci);
    List<Cliente> buscarPorEstadoVerificacion(String estado);
    List<Cliente> buscarConCreditoAprobado();
    void aprobarCredito(Long clienteId, double montoAprobado);
    void rechazarCredito(Long clienteId, String motivo);
    void actualizarEstadoVerificacion(Long clienteId, String estado, String observaciones, Long usuarioId);
}
