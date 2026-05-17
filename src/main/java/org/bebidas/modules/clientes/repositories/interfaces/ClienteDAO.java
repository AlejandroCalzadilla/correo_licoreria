package org.bebidas.modules.clientes.repositories.interfaces;

import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.clientes.Cliente;

public interface ClienteDAO extends GenericDAO<Cliente, Long> {
    List<Cliente> buscarPorNombre(String nombre);
    List<Cliente> buscarPorCi(String ci);
    List<Cliente> buscarPorEstadoVerificacion(String estado);
    List<Cliente> buscarConCreditoAprobado();
    java.util.Optional<Cliente> findByUsuarioId(Long usuarioId);
}
