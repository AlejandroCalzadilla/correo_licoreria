package org.bebidas.modules.dao.impl;

import org.bebidas.infraestructure.conexion.*;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.repositories.interfaces.ClienteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOImpl extends GenericDAOImpl<Cliente, Long> implements ClienteDAO {

    public ClienteDAOImpl() {
        super(Cliente.class);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public Cliente save(Cliente cliente) {
        if (cliente.getId() == null) {
            return insert(cliente);
        } else {
            return update(cliente);
        }
    }

    private Cliente insert(Cliente cliente) {
        String sql = "INSERT INTO cliente (ci, nombre, telefono, direccion, estado, usuario_id, credito_aprobado, limite_credito) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getCi());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, String.valueOf(cliente.getEstado()));
            stmt.setObject(6, cliente.getUsuario() != null ? cliente.getUsuario().getId() : null);
            stmt.setBoolean(7, cliente.isCreditoAprobado());
            stmt.setDouble(8, cliente.getLimiteCredito());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    private Cliente update(Cliente cliente) {
        String sql = "UPDATE cliente SET ci = ?, nombre = ?, telefono = ?, direccion = ?, estado = ?, credito_aprobado = ?, limite_credito = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCi());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, String.valueOf(cliente.getEstado()));
            stmt.setBoolean(6, cliente.isCreditoAprobado());
            stmt.setDouble(7, cliente.getLimiteCredito());

            stmt.setLong(9, cliente.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE LOWER(nombre) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarPorCi(String ci) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE ci = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ci);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarPorEstadoVerificacion(String estado) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE estado_verificacion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarConCreditoAprobado() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE credito_aprobado = true";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setCi(rs.getString("ci"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDireccion(rs.getString("direccion"));

        String estado = rs.getString("estado");
        if (estado != null && !estado.isEmpty()) {
            cliente.setEstado(estado.charAt(0));
        }

        cliente.setCreditoAprobado(rs.getBoolean("credito_aprobado"));
        cliente.setLimiteCredito(rs.getDouble("limite_credito"));

        return cliente;
    }
}
