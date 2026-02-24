package org.bebidas.modules.compras.repositories;

import org.bebidas.core.util.GenericDAOImpl;
import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.compras.Compra;
import org.bebidas.modules.compras.DetalleCompra;
import org.bebidas.modules.compras.repositories.interfaces.DetalleCompraDAO;
import org.bebidas.modules.inventario.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleCompraDAOImpl extends GenericDAOImpl<DetalleCompra, Long> implements DetalleCompraDAO {

    public DetalleCompraDAOImpl() {
        super(DetalleCompra.class);
    }

    private DetalleCompra mapResultSetToDetalleCompra(ResultSet rs) throws SQLException {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setId(rs.getLong("id"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        
        // Compra relationship
        Long compraId = rs.getLong("compra_id");
        if (rs.wasNull()) {
            compraId = null;
        }
        if (compraId != null) {
            Compra compra = new Compra();
            compra.setId(compraId);
            detalle.setCompra(compra);
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
    public Optional<DetalleCompra> findById(Long id) {
        String sql = "SELECT * FROM detalle_compra WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToDetalleCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<DetalleCompra> findAll() {
        List<DetalleCompra> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_compra";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                detalles.add(mapResultSetToDetalleCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }

    @Override
    public DetalleCompra save(DetalleCompra detalle) {
        if (detalle.getId() == null) {
            return insertar(detalle);
        } else {
            return actualizar(detalle);
        }
    }

    public DetalleCompra insertar(DetalleCompra detalle) {
        String sql = "INSERT INTO detalle_compra (compra_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (detalle.getCompra() != null) {
                stmt.setLong(1, detalle.getCompra().getId());
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

    public DetalleCompra actualizar(DetalleCompra detalle) {
        String sql = "UPDATE detalle_compra SET compra_id = ?, producto_id = ?, cantidad = ?, precio_unitario = ?, subtotal = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (detalle.getCompra() != null) {
                stmt.setLong(1, detalle.getCompra().getId());
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
        String sql = "DELETE FROM detalle_compra WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(Long id) {
        delete(id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM detalle_compra WHERE id = ?";
        
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
    public List<DetalleCompra> buscarPorCompra(Long compraId) {
        List<DetalleCompra> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_compra WHERE compra_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, compraId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapResultSetToDetalleCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }

    @Override
    public List<DetalleCompra> buscarPorProducto(Long productoId) {
        List<DetalleCompra> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_compra WHERE producto_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapResultSetToDetalleCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }
}