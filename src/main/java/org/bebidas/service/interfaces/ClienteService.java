package org.bebidas.service.interfaces;

import org.bebidas.model.Cliente;

import java.util.List;

public interface ClienteService extends GenericService<Cliente, Long> {
    List<Cliente> buscarPorNombre(String nombre);
    List<Cliente> buscarPorCi(String ci);
    List<Cliente> buscarPorEstadoVerificacion(String estado);
    List<Cliente> buscarConCreditoAprobado();
    void aprobarCredito(Long clienteId, double montoAprobado);
    void rechazarCredito(Long clienteId, String motivo);
    void actualizarEstadoVerificacion(Long clienteId, String estado, String observaciones, Long usuarioId);
}
