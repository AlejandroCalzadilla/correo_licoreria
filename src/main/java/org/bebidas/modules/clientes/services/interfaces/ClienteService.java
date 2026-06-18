package org.bebidas.modules.clientes.services.interfaces;

import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.service.interfaces.GenericService;

public interface ClienteService extends GenericService<Cliente, Long> {

    java.util.Optional<Cliente> findByUsuarioId(Long usuarioId);
}
