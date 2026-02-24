package org.bebidas.modules.carrito.repositories;

import org.bebidas.core.util.GenericDAOImpl;
import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.carrito.repositories.interfaces.ItemCarritoDAO;
import org.bebidas.modules.inventario.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemCarritoDAOImpl extends GenericDAOImpl<ItemCarrito, Long> implements ItemCarritoDAO {

    public ItemCarritoDAOImpl() {
        super(ItemCarrito.class);
    }

    private ItemCarrito mapResultSetToItemCarrito(ResultSet rs) throws SQLException {
        ItemCarrito item = new ItemCarrito();
        item.setId(rs.getLong("id"));
        item.setCantidad(rs.getInt("cantidad"));
        item.setPrecio(rs.getBigDecimal("precio"));
        item.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        item.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
        
        // Carrito relationship
        Long carritoId = rs.getLong("carrito_id");
        if (rs.wasNull()) {
            carritoId = null;
        }
        if (carritoId != null) {
            Carrito carrito = new Carrito();
            carrito.setId(carritoId);
            item.setCarrito(carrito);
        }
        
        // Producto relationship
        Long productoId = rs.getLong("producto_id");
        if (rs.wasNull()) {
            productoId = null;
        }
        if (productoId != null) {
            Producto producto = new Producto();
            producto.setId(productoId);
            item.setProducto(producto);
        }
        
        return item;
    }

    @Override
    public Optional<ItemCarrito> findById(Long id) {
        String sql = "SELECT * FROM item_carrito WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToItemCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<ItemCarrito> findAll() {
        List<ItemCarrito> items = new ArrayList<>();
        String sql = "SELECT * FROM item_carrito";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapResultSetToItemCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }

    @Override
    public ItemCarrito save(ItemCarrito item) {
        if (item.getId() == null) {
            return insert(item);
        } else {
            return update(item);
        }
    }

    private ItemCarrito insert(ItemCarrito item) {
        String sql = "INSERT INTO item_carrito (carrito_id, producto_id, cantidad, precio, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (item.getCarrito() != null) {
                stmt.setLong(1, item.getCarrito().getId());
            } else {
                stmt.setNull(1, java.sql.Types.BIGINT);
            }
            if (item.getProducto() != null) {
                stmt.setLong(2, item.getProducto().getId());
            } else {
                stmt.setNull(2, java.sql.Types.BIGINT);
            }
            stmt.setInt(3, item.getCantidad());
            stmt.setBigDecimal(4, item.getPrecio());
            stmt.setTimestamp(5, item.getCreatedAt() != null ? Timestamp.valueOf(item.getCreatedAt()) : null);
            stmt.setTimestamp(6, item.getUpdatedAt() != null ? Timestamp.valueOf(item.getUpdatedAt()) : null);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return item;
    }

    private ItemCarrito update(ItemCarrito item) {
        String sql = "UPDATE item_carrito SET carrito_id = ?, producto_id = ?, cantidad = ?, precio = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (item.getCarrito() != null) {
                stmt.setLong(1, item.getCarrito().getId());
            } else {
                stmt.setNull(1, java.sql.Types.BIGINT);
            }
            if (item.getProducto() != null) {
                stmt.setLong(2, item.getProducto().getId());
            } else {
                stmt.setNull(2, java.sql.Types.BIGINT);
            }
            stmt.setInt(3, item.getCantidad());
            stmt.setBigDecimal(4, item.getPrecio());
            stmt.setTimestamp(5, item.getUpdatedAt() != null ? Timestamp.valueOf(item.getUpdatedAt()) : null);
            stmt.setLong(6, item.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return item;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM item_carrito WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM item_carrito WHERE id = ?";
        
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
    public List<ItemCarrito> buscarPorCarrito(Long carritoId) {
        List<ItemCarrito> items = new ArrayList<>();
        String sql = "SELECT * FROM item_carrito WHERE carrito_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, carritoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItemCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }

    @Override
    public List<ItemCarrito> buscarPorProducto(Long productoId) {
        List<ItemCarrito> items = new ArrayList<>();
        String sql = "SELECT * FROM item_carrito WHERE producto_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, productoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItemCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }

    @Override
    public boolean existeProductoEnCarrito(Long carritoId, Long productoId) {
        String sql = "SELECT COUNT(*) FROM item_carrito WHERE carrito_id = ? AND producto_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, carritoId);
            stmt.setLong(2, productoId);
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
    public void actualizarCantidad(Long itemId, int nuevaCantidad) {
        String sql = "UPDATE item_carrito SET cantidad = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nuevaCantidad);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarPorCarrito(Long carritoId) {
        String sql = "DELETE FROM item_carrito WHERE carrito_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, carritoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
