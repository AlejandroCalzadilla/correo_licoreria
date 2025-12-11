package org.bebidas.modules.dao.impl;

import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.dao.interfaces.CreditoDAO;
import org.bebidas.modules.ventas.Venta;

import java.math.BigDecimal;
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

public class CreditoDAOImpl extends GenericDAOImpl<Credito, Long> implements CreditoDAO {

    public CreditoDAOImpl() {
        super(Credito.class);
    }

    private Credito mapResultSetToCredito(ResultSet rs) throws SQLException {
        Credito credito = new Credito();
        credito.setId(rs.getLong("id"));
        credito.setMontoTotal(rs.getBigDecimal("monto_total"));
        credito.setSaldo(rs.getBigDecimal("saldo"));
        credito.setNumeroCuotas(rs.getString("numero_cuotas"));
        credito.setEstado(rs.getString("estado"));
        credito.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        
        // Venta relationship
        Long ventaId = rs.getLong("venta_id");
        if (rs.wasNull()) {
            ventaId = null;
        }
        if (ventaId != null) {
            Venta venta = new Venta();
            venta.setId(ventaId);
            credito.setVenta(venta);
        }
        
        return credito;
    }

    @Override
    public Optional<Credito> findById(Long id) {
        String sql = "SELECT * FROM creditos WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCredito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Credito> findAll() {
        List<Credito> creditos = new ArrayList<>();
        String sql = "SELECT * FROM creditos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                creditos.add(mapResultSetToCredito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return creditos;
    }

    @Override
    public Credito save(Credito credito) {
        if (credito.getId() == null) {
            return insert(credito);
        } else {
            return update(credito);
        }
    }

    private Credito insert(Credito credito) {
        String sql = "INSERT INTO creditos (monto_total, saldo, numero_cuotas, estado, fecha_inicio, venta_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setBigDecimal(1, credito.getMontoTotal());
            stmt.setBigDecimal(2, credito.getSaldo());
            stmt.setString(3, credito.getNumeroCuotas());
            stmt.setString(4, credito.getEstado());
            stmt.setDate(5, Date.valueOf(credito.getFechaInicio()));
            if (credito.getVenta() != null) {
                stmt.setLong(6, credito.getVenta().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        credito.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return credito;
    }

    private Credito update(Credito credito) {
        String sql = "UPDATE creditos SET monto_total = ?, saldo = ?, numero_cuotas = ?, estado = ?, fecha_inicio = ?, venta_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, credito.getMontoTotal());
            stmt.setBigDecimal(2, credito.getSaldo());
            stmt.setString(3, credito.getNumeroCuotas());
            stmt.setString(4, credito.getEstado());
            stmt.setDate(5, Date.valueOf(credito.getFechaInicio()));
            if (credito.getVenta() != null) {
                stmt.setLong(6, credito.getVenta().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }
            stmt.setLong(7, credito.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return credito;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM creditos WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM creditos WHERE id = ?";
        
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
    public List<Credito> buscarPorVenta(Long ventaId) {
        List<Credito> creditos = new ArrayList<>();
        String sql = "SELECT * FROM creditos WHERE venta_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, ventaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                creditos.add(mapResultSetToCredito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return creditos;
    }

    @Override
    public List<Credito> buscarPorEstado(String estado) {
        List<Credito> creditos = new ArrayList<>();
        String sql = "SELECT * FROM creditos WHERE estado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                creditos.add(mapResultSetToCredito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return creditos;
    }

    @Override
    public List<Credito> buscarPorCliente(Long clienteId) {
        List<Credito> creditos = new ArrayList<>();
        String sql = "SELECT c.* FROM creditos c JOIN ventas v ON c.venta_id = v.id WHERE v.cliente_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                creditos.add(mapResultSetToCredito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return creditos;
    }

    @Override
    public List<Credito> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        List<Credito> creditos = new ArrayList<>();
        String sql = "SELECT * FROM creditos WHERE fecha_inicio BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fin));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                creditos.add(mapResultSetToCredito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return creditos;
    }

    @Override
    public BigDecimal obtenerSaldoPendientePorCliente(Long clienteId) {
        String sql = "SELECT COALESCE(SUM(c.saldo), 0) FROM creditos c JOIN ventas v ON c.venta_id = v.id WHERE v.cliente_id = ? AND c.estado = 'PENDIENTE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }

    @Override
    public List<Credito> buscarCreditosVencidos() {
        List<Credito> creditos = new ArrayList<>();
        String sql = "SELECT c.* FROM creditos c JOIN ventas v ON c.venta_id = v.id WHERE c.estado = 'PENDIENTE' AND v.fecha_vencimiento < CURDATE()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                creditos.add(mapResultSetToCredito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return creditos;
    }
}
