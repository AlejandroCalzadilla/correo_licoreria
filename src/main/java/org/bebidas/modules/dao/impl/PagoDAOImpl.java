package org.bebidas.modules.dao.impl;

import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.dao.interfaces.PagoDAO;
import org.bebidas.modules.ventas.Pago;
import org.bebidas.modules.ventas.Venta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagoDAOImpl extends GenericDAOImpl<Pago, Long> implements PagoDAO {

    public PagoDAOImpl() {
        super(Pago.class);
    }

    private Pago mapResultSetToPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setId(rs.getLong("id"));
        pago.setNroPago(rs.getString("nro_pago"));
        pago.setTipoPago(rs.getString("tipo_pago"));
        pago.setEstado(rs.getString("estado"));
        pago.setMonto(rs.getBigDecimal("monto"));
        pago.setQrImage(rs.getString("qr_image"));
        pago.setNroTransaccion(rs.getString("nro_transaccion"));
        pago.setNombrePersona(rs.getString("nombre_persona"));
        pago.setEmail(rs.getString("email"));
        pago.setTelefono(rs.getString("telefono"));
        pago.setNit(rs.getString("nit"));
        pago.setDetallesPago(rs.getString("detalles_pago"));
        pago.setFechaPago(rs.getTimestamp("fecha_pago") != null ? rs.getTimestamp("fecha_pago").toLocalDateTime() : null);
        pago.setFechaConfirmacion(rs.getTimestamp("fecha_confirmacion") != null ? rs.getTimestamp("fecha_confirmacion").toLocalDateTime() : null);
        pago.setObservaciones(rs.getString("observaciones"));
        pago.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        pago.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
        
        // Venta relationship
        Long ventaId = rs.getLong("venta_id");
        if (rs.wasNull()) {
            ventaId = null;
        }
        if (ventaId != null) {
            Venta venta = new Venta();
            venta.setId(ventaId);
            pago.setVenta(venta);
        }
        
        return pago;
    }

    @Override
    public Optional<Pago> findById(Long id) {
        String sql = "SELECT * FROM pago WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Pago> findAll() {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago ORDER BY fecha_pago DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pagos;
    }

    @Override
    public Pago save(Pago pago) {
        if (pago.getId() == null) {
            return insert(pago);
        } else {
            return update(pago);
        }
    }

    private Pago insert(Pago pago) {
        String sql = "INSERT INTO pago (venta_id, nro_pago, tipo_pago, estado, monto, qr_image, nro_transaccion, nombre_persona, email, telefono, nit, detalles_pago, fecha_pago, fecha_confirmacion, observaciones, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (pago.getVenta() != null) {
                stmt.setLong(1, pago.getVenta().getId());
            } else {
                stmt.setNull(1, java.sql.Types.BIGINT);
            }
            stmt.setString(2, pago.getNroPago());
            stmt.setString(3, pago.getTipoPago());
            stmt.setString(4, pago.getEstado());
            stmt.setBigDecimal(5, pago.getMonto());
            stmt.setString(6, pago.getQrImage());
            stmt.setString(7, pago.getNroTransaccion());
            stmt.setString(8, pago.getNombrePersona());
            stmt.setString(9, pago.getEmail());
            stmt.setString(10, pago.getTelefono());
            stmt.setString(11, pago.getNit());
            stmt.setString(12, pago.getDetallesPago());
            stmt.setTimestamp(13, pago.getFechaPago() != null ? Timestamp.valueOf(pago.getFechaPago()) : null);
            stmt.setTimestamp(14, pago.getFechaConfirmacion() != null ? Timestamp.valueOf(pago.getFechaConfirmacion()) : null);
            stmt.setString(15, pago.getObservaciones());
            stmt.setTimestamp(16, pago.getCreatedAt() != null ? Timestamp.valueOf(pago.getCreatedAt()) : null);
            stmt.setTimestamp(17, pago.getUpdatedAt() != null ? Timestamp.valueOf(pago.getUpdatedAt()) : null);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pago.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pago;
    }

    private Pago update(Pago pago) {
        String sql = "UPDATE pago SET venta_id = ?, nro_pago = ?, tipo_pago = ?, estado = ?, monto = ?, qr_image = ?, nro_transaccion = ?, nombre_persona = ?, email = ?, telefono = ?, nit = ?, detalles_pago = ?, fecha_pago = ?, fecha_confirmacion = ?, observaciones = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (pago.getVenta() != null) {
                stmt.setLong(1, pago.getVenta().getId());
            } else {
                stmt.setNull(1, java.sql.Types.BIGINT);
            }
            stmt.setString(2, pago.getNroPago());
            stmt.setString(3, pago.getTipoPago());
            stmt.setString(4, pago.getEstado());
            stmt.setBigDecimal(5, pago.getMonto());
            stmt.setString(6, pago.getQrImage());
            stmt.setString(7, pago.getNroTransaccion());
            stmt.setString(8, pago.getNombrePersona());
            stmt.setString(9, pago.getEmail());
            stmt.setString(10, pago.getTelefono());
            stmt.setString(11, pago.getNit());
            stmt.setString(12, pago.getDetallesPago());
            stmt.setTimestamp(13, pago.getFechaPago() != null ? Timestamp.valueOf(pago.getFechaPago()) : null);
            stmt.setTimestamp(14, pago.getFechaConfirmacion() != null ? Timestamp.valueOf(pago.getFechaConfirmacion()) : null);
            stmt.setString(15, pago.getObservaciones());
            stmt.setTimestamp(16, pago.getUpdatedAt() != null ? Timestamp.valueOf(pago.getUpdatedAt()) : null);
            stmt.setLong(17, pago.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pago;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM pago WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM pago WHERE id = ?";
        
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
    public List<Pago> buscarPorVenta(Long ventaId) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE venta_id = ? ORDER BY fecha_pago DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, ventaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pagos;
    }

    @Override
    public List<Pago> buscarPorEstado(String estado) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE estado = ? ORDER BY fecha_pago DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pagos;
    }

    @Override
    public List<Pago> buscarPorTipoPago(String tipoPago) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE tipo_pago = ? ORDER BY fecha_pago DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipoPago);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pagos;
    }

    @Override
    public List<Pago> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE DATE(fecha_pago) BETWEEN ? AND ? ORDER BY fecha_pago DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(inicio));
            stmt.setDate(2, java.sql.Date.valueOf(fin));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pagos;
    }

    @Override
    public List<Pago> buscarPorCliente(Long clienteId) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT p.* FROM pago p JOIN ventas v ON p.venta_id = v.id WHERE v.cliente_id = ? ORDER BY p.fecha_pago DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pagos;
    }

    @Override
    public BigDecimal obtenerTotalPagosPorVenta(Long ventaId) {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pago WHERE venta_id = ? AND estado = 'CONFIRMADO'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, ventaId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
}
