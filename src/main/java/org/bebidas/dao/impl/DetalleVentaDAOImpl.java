package org.bebidas.dao.impl;

import org.bebidas.conexion.DatabaseConnection;
import org.bebidas.dao.interfaces.DetalleVentaDAO;
import org.bebidas.model.DetalleVenta;
import org.bebidas.model.Producto;
import org.bebidas.model.Venta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleVentaDAOImpl extends GenericDAOImpl<DetalleVenta, Long> implements DetalleVentaDAO {

    public DetalleVentaDAOImpl() {
        super(DetalleVenta.class);
    }

    private DetalleVenta mapResultSetToDetalleVenta(ResultSet rs) throws SQLException {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(rs.getLong("id"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.getSubtotal();
        
        // Venta relationship
        Long ventaId = rs.getLong("venta_id");
        if (rs.wasNull()) {
            ventaId = null;
        }
        if (ventaId != null) {
            Venta venta = new Venta();
            venta.setId(ventaId);
            detalle.setVenta(venta);
        }
        
        // Producto relationship
        Long productoId = rs.getLong("producto_id");
        if (rs.wasNull()) {
            productoId = null;
        }
        if (productoId != null) {
            Producto producto = new Producto();
            producto.setId(productoId);
            detalle.setProducto(producto);
        }
        
        return detalle;
    }

    @Override
    public Optional<DetalleVenta> findById(Long id) {
        String sql = "SELECT * FROM detalle_venta WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToDetalleVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<DetalleVenta> findAll() {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                detalles.add(mapResultSetToDetalleVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }

    @Override
    public DetalleVenta save(DetalleVenta detalle) {
        if (detalle.getId() == null) {
            return insert(detalle);
        } else {
            return update(detalle);
        }
    }

    private DetalleVenta insert(DetalleVenta detalle) {
        String sql = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (detalle.getVenta() != null) {
                stmt.setLong(1, detalle.getVenta().getId());
            } else {
                stmt.setNull(1, java.sql.Types.BIGINT);
            }
            if (detalle.getProducto() != null) {
                stmt.setLong(2, detalle.getProducto().getId());
            } else {
                stmt.setNull(2, java.sql.Types.BIGINT);
            }
            stmt.setInt(3, detalle.getCantidad());
            stmt.setBigDecimal(4, detalle.getPrecioUnitario());
            stmt.setBigDecimal(5, detalle.getSubtotal());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        detalle.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalle;
    }

    private DetalleVenta update(DetalleVenta detalle) {
        String sql = "UPDATE detalle_venta SET venta_id = ?, producto_id = ?, cantidad = ?, precio_unitario = ?, subtotal = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (detalle.getVenta() != null) {
                stmt.setLong(1, detalle.getVenta().getId());
            } else {
                stmt.setNull(1, java.sql.Types.BIGINT);
            }
            if (detalle.getProducto() != null) {
                stmt.setLong(2, detalle.getProducto().getId());
            } else {
                stmt.setNull(2, java.sql.Types.BIGINT);
            }
            stmt.setInt(3, detalle.getCantidad());
            stmt.setBigDecimal(4, detalle.getPrecioUnitario());
            stmt.setBigDecimal(5, detalle.getSubtotal());
            stmt.setLong(6, detalle.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalle;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM detalle_venta WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM detalle_venta WHERE id = ?";
        
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
    public List<DetalleVenta> buscarPorVenta(Long ventaId) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta WHERE venta_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, ventaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapResultSetToDetalleVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }

    @Override
    public List<DetalleVenta> buscarPorProducto(Long productoId) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta WHERE producto_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapResultSetToDetalleVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }

    @Override
    public BigDecimal obtenerTotalVentasPorProducto(Long productoId) {
        String sql = "SELECT COALESCE(SUM(subtotal), 0) FROM detalle_venta WHERE producto_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productoId);
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
    public Integer obtenerCantidadVendidaPorProducto(Long productoId) {
        String sql = "SELECT COALESCE(SUM(cantidad), 0) FROM detalle_venta WHERE producto_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productoId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}
