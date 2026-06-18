package org.bebidas.modules.clientes.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.repositories.interfaces.ClienteDAO;
import org.bebidas.modules.clientes.services.interfaces.ClienteService;
import java.util.Optional;

public class ClienteServiceImpl extends GenericServiceImpl<Cliente, Long> implements ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteServiceImpl(ClienteDAO clienteDAO) {
        super(clienteDAO);
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Optional<Cliente> findByUsuarioId(Long usuarioId) {
        return clienteDAO.findByUsuarioId(usuarioId);
    }
}
