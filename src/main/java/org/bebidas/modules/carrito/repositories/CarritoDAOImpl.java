package org.bebidas.modules.carrito.repositories;

import org.bebidas.infraestructure.conexion.*;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.repositories.interfaces.CarritoDAO;
import org.bebidas.modules.dao.impl.GenericDAOImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarritoDAOImpl extends GenericDAOImpl<Carrito, Long> implements CarritoDAO {

    public CarritoDAOImpl() {
        super(Carrito.class);
    }

    @Override
    public Optional<Carrito> findById(Long id) {
        String sql = "SELECT * FROM carrito WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Carrito> findAll() {
        List<Carrito> carritos = new ArrayList<>();
        String sql = "SELECT * FROM carrito";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                carritos.add(mapResultSetToCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carritos;
    }

    @Override
    public Carrito save(Carrito carrito) {
        if (carrito.getId() == null) {
            return insert(carrito);
        } else {
            return update(carrito);
        }
    }

    private Carrito insert(Carrito carrito) {
        String sql = "INSERT INTO carrito (session_id, usuario_id, created_at, updated_at) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, carrito.getSessionId());
            stmt.setLong(2, carrito.getUsuario() != null ? carrito.getUsuario().getId() : null);
            stmt.setTimestamp(3, Timestamp.valueOf(carrito.getCreatedAt()));
            stmt.setTimestamp(4, Timestamp.valueOf(carrito.getUpdatedAt()));
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                carrito.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carrito;
    }

    private Carrito update(Carrito carrito) {
        String sql = "UPDATE carrito SET session_id = ?, usuario_id = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, carrito.getSessionId());
            stmt.setLong(2, carrito.getUsuario() != null ? carrito.getUsuario().getId() : null);
            stmt.setTimestamp(3, Timestamp.valueOf(carrito.getUpdatedAt()));
            stmt.setLong(4, carrito.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carrito;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM carrito WHERE id = ?";
        
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
    public List<Carrito> buscarPorCliente(Long clienteId) {
        List<Carrito> carritos = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE usuario_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                carritos.add(mapResultSetToCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carritos;
    }

   
    @Override
    public Carrito buscarActivoPorCliente(Long clienteId) {
        String sql = "SELECT * FROM carrito WHERE usuario_id = ?  LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCarrito(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Carrito> buscarPorRangoFechas(String fechaInicio, String fechaFin) {
        List<Carrito> carritos = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                carritos.add(mapResultSetToCarrito(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carritos;
    }

    private Carrito mapResultSetToCarrito(ResultSet rs) throws SQLException {
        Carrito carrito = new Carrito();
        carrito.setId(rs.getLong("id"));
        carrito.setSessionId(rs.getString("session_id"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            carrito.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            carrito.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // TODO: Cargar el usuario si es necesario
        // Long usuarioId = rs.getLong("usuario_id");
        
        return carrito;
    }
}
