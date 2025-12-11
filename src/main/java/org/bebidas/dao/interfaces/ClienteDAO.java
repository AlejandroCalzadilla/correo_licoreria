package org.bebidas.dao.interfaces;

import org.bebidas.model.Cliente;

import java.util.List;

public interface ClienteDAO extends GenericDAO<Cliente, Long> {
    List<Cliente> buscarPorNombre(String nombre);
    List<Cliente> buscarPorCi(String ci);
    List<Cliente> buscarPorEstadoVerificacion(String estado);
    List<Cliente> buscarConCreditoAprobado();
}
