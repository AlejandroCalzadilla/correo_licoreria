package org.bebidas.modules.clientes.repositories.interfaces;

import java.util.List;

import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.dao.interfaces.GenericDAO;

public interface ClienteDAO extends GenericDAO<Cliente, Long> {
    List<Cliente> buscarPorNombre(String nombre);
    List<Cliente> buscarPorCi(String ci);
    List<Cliente> buscarPorEstadoVerificacion(String estado);
    List<Cliente> buscarConCreditoAprobado();
}
