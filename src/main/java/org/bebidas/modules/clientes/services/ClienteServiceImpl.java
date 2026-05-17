package org.bebidas.modules.clientes.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.repositories.interfaces.ClienteDAO;
import org.bebidas.modules.clientes.services.interfaces.ClienteService;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.usuarios.repositories.UsuarioDAO;



import java.util.Optional;

public class ClienteServiceImpl extends GenericServiceImpl<Cliente, Long> implements ClienteService {

    private final ClienteDAO clienteDAO;
    private final UsuarioDAO usuarioDAO;

    
    public ClienteServiceImpl(ClienteDAO clienteDAO, UsuarioDAO usuarioDAO) {
        super(clienteDAO);
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public void aprobarCredito(Long clienteId, double montoAprobado) {
        Cliente cliente = findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        if (montoAprobado <= 0) {
            throw new IllegalArgumentException("El monto aprobado debe ser mayor a cero");
        }
        cliente.setCreditoAprobado(true);
        cliente.setLimiteCredito(montoAprobado);
        save(cliente);
    }

    @Override
    public void rechazarCredito(Long clienteId, String motivo) {
        Cliente cliente = findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        cliente.setCreditoAprobado(false);
        cliente.setLimiteCredito(0);
        // Aquí podrías agregar el motivo de rechazo a las observaciones
        save(cliente);
    }

    @Override
    public void actualizarEstadoVerificacion(Long clienteId, String estado, String observaciones, Long usuarioId) {
        Cliente cliente = findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        Usuario usuario = usuarioDAO.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));        
        cliente.setVerificadoPor(usuario);
        save(cliente);
     }

    @Override
    public Optional<Cliente> findByUsuarioId(Long usuarioId) {
        return clienteDAO.findByUsuarioId(usuarioId);
    }
}
