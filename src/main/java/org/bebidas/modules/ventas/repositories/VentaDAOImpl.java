package org.bebidas.modules.ventas.repositories;

import org.bebidas.core.util.GenericDAOImpl;
import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.ventas.Venta;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaDAOImpl extends GenericDAOImpl<Venta, Long> implements VentaDAO {

    public VentaDAOImpl() {
        super(Venta.class);
    }

    private Venta mapResultSetToVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getLong("id"));
        venta.setNroVenta(rs.getString("nro_venta"));
        venta.setFecha(rs.getDate("fecha").toLocalDate());
        venta.setTipo(rs.getString("tipo"));
        venta.setMontoTotal(rs.getBigDecimal("monto_total"));
        venta.setSaldo(rs.getBigDecimal("saldo"));
        venta.setNumeroCuotas(rs.getString("numero_cuotas"));
        venta.setEstado(rs.getString("estado"));
        venta.setMetodoPago(rs.getString("metodo_pago"));
        venta.setEstadoPago(rs.getString("estado_pago"));
        
        // Cliente relationship
        Long clienteId = rs.getLong("cliente_id");
        if (rs.wasNull()) {
            clienteId = null;
        }
        if (clienteId != null) {
            Cliente cliente = new Cliente();
            cliente.setId(clienteId);
            venta.setCliente(cliente);
        }
        
        // Usuario relationship
        Long usuarioId = rs.getLong("usuario_id");
        if (rs.wasNull()) {
            usuarioId = null;
        }
        if (usuarioId != null) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            venta.setUsuario(usuario);
        }
        
        return venta;
    }

    @Override
    public Optional<Venta> findById(Long id) {
        String sql = "SELECT * FROM venta WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Venta> findAll() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }

    @Override
    public Venta save(Venta venta) {
        if (venta.getId() == null) {
            return insert(venta);
        } else {
            return update(venta);
        }
    }

    private Venta insert(Venta venta) {
        String sql = "INSERT INTO venta (nro_venta, fecha, tipo, monto_total, saldo, numero_cuotas, estado, cliente_id, metodo_pago, estado_pago, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, venta.getNroVenta());
            stmt.setDate(2, Date.valueOf(venta.getFecha()));
            stmt.setString(3, venta.getTipo());
            stmt.setBigDecimal(4, venta.getMontoTotal());
            stmt.setBigDecimal(5, venta.getSaldo());
            stmt.setString(6, venta.getNumeroCuotas());
            stmt.setString(7, venta.getEstado());
            if (venta.getCliente() != null) {
                stmt.setLong(8, venta.getCliente().getId());
            } else {
                stmt.setNull(8, java.sql.Types.BIGINT);
            }
            stmt.setString(9, venta.getMetodoPago());
            stmt.setString(10, venta.getEstadoPago());
            if (venta.getUsuario() != null) {
                stmt.setLong(11, venta.getUsuario().getId());
            } else {
                stmt.setNull(11, java.sql.Types.BIGINT);
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venta.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return venta;
    }

    private Venta update(Venta venta) {
        String sql = "UPDATE venta SET nro_venta = ?, fecha = ?, tipo = ?, monto_total = ?, saldo = ?, numero_cuotas = ?, estado = ?, cliente_id = ?, metodo_pago = ?, estado_pago = ?, usuario_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, venta.getNroVenta());
            stmt.setDate(2, Date.valueOf(venta.getFecha()));
            stmt.setString(3, venta.getTipo());
            stmt.setBigDecimal(4, venta.getMontoTotal());
            stmt.setBigDecimal(5, venta.getSaldo());
            stmt.setString(6, venta.getNumeroCuotas());
            stmt.setString(7, venta.getEstado());
            if (venta.getCliente() != null) {
                stmt.setLong(8, venta.getCliente().getId());
            } else {
                stmt.setNull(8, java.sql.Types.BIGINT);
            }
            stmt.setString(9, venta.getMetodoPago());
            stmt.setString(10, venta.getEstadoPago());
            if (venta.getUsuario() != null) {
                stmt.setLong(11, venta.getUsuario().getId());
            } else {
                stmt.setNull(11, java.sql.Types.BIGINT);
            }
            stmt.setLong(12, venta.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return venta;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM venta WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM ventas WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public List<Venta> buscarPorFecha(LocalDate fecha) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE fecha = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }

    @Override
    public List<Venta> buscarPorCliente(Long clienteId) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE cliente_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }

    @Override
    public List<Venta> buscarPorEstado(String estado) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE estado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }

    @Override
    public List<Venta> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE fecha BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fin));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }

    @Override
    public List<Venta> buscarPorUsuario(Long usuarioId) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }
}
