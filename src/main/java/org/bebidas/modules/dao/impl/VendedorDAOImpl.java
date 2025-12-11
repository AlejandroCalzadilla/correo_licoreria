package org.bebidas.modules.dao.impl;

import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.dao.interfaces.VendedorDAO;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.vendedores.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendedorDAOImpl extends GenericDAOImpl<Vendedor, Long> implements VendedorDAO {

    public VendedorDAOImpl() {
        super(Vendedor.class);
    }

    private Vendedor mapResultSetToVendedor(ResultSet rs) throws SQLException {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(rs.getLong("id"));
        vendedor.setCi(rs.getString("ci"));
        vendedor.setNombre(rs.getString("nombre"));
        
        // Usuario relationship
        Long usuarioId = rs.getLong("usuario_id");
        if (rs.wasNull()) {
            usuarioId = null;
        }
        if (usuarioId != null) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            vendedor.setUsuario(usuario);
        }
        
        return vendedor;
    }

    @Override
    public Optional<Vendedor> findById(Long id) {
        String sql = "SELECT * FROM vendedor WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToVendedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Vendedor> findAll() {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM vendedor";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                vendedores.add(mapResultSetToVendedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vendedores;
    }

    @Override
    public Vendedor save(Vendedor vendedor) {
        if (vendedor.getId() == null) {
            return insert(vendedor);
        } else {
            return update(vendedor);
        }
    }

    private Vendedor insert(Vendedor vendedor) {
        String sql = "INSERT INTO vendedor (ci, nombre, usuario_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, vendedor.getCi());
            stmt.setString(2, vendedor.getNombre());
            if (vendedor.getUsuario() != null) {
                stmt.setLong(3, vendedor.getUsuario().getId());
            } else {
                stmt.setNull(3, java.sql.Types.BIGINT);
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vendedor.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vendedor;
    }

    private Vendedor update(Vendedor vendedor) {
        String sql = "UPDATE vendedor SET ci = ?, nombre = ?, usuario_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vendedor.getCi());
            stmt.setString(2, vendedor.getNombre());
            if (vendedor.getUsuario() != null) {
                stmt.setLong(3, vendedor.getUsuario().getId());
            } else {
                stmt.setNull(3, java.sql.Types.BIGINT);
            }
            stmt.setLong(4, vendedor.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vendedor;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM vendedor WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM vendedor WHERE id = ?";
        
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
    public List<Vendedor> buscarPorNombre(String nombre) {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM vendedor WHERE LOWER(nombre) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                vendedores.add(mapResultSetToVendedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vendedores;
    }

    @Override
    public Vendedor buscarPorUsuario(Long usuarioId) {
        String sql = "SELECT * FROM vendedor WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVendedor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<Vendedor> buscarPorCi(String ci) {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM vendedor WHERE ci = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ci);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                vendedores.add(mapResultSetToVendedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vendedores;
    }
}
