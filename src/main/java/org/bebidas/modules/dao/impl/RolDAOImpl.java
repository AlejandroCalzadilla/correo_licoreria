package org.bebidas.modules.dao.impl;

import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.dao.interfaces.RolDAO;
import org.bebidas.modules.usuarios.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolDAOImpl extends GenericDAOImpl<Rol, Long> implements RolDAO {

    public RolDAOImpl() {
        super(Rol.class);
    }

    private Rol mapResultSetToRol(ResultSet rs) throws SQLException {
        Rol rol = new Rol();
        rol.setId(rs.getLong("id"));
        rol.setNombre(rs.getString("nombre"));
        rol.setDescripcion(rs.getString("descripcion"));
       
        
        return rol;
    }

    @Override
    public Optional<Rol> findById(Long id) {
        String sql = "SELECT * FROM rol WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToRol(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Rol> findAll() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM rol";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                roles.add(mapResultSetToRol(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return roles;
    }

    @Override
    public Rol save(Rol rol) {
        if (rol.getId() == null) {
            return insert(rol);
        } else {
            return update(rol);
        }
    }

    private Rol insert(Rol rol) {
        String sql = "INSERT INTO rol (nombre, descripcion) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, rol.getNombre());
            stmt.setString(2, rol.getDescripcion());
          
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rol.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rol;
    }

    private Rol update(Rol rol) {
        String sql = "UPDATE rol SET nombre = ?, descripcion = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rol.getNombre());
            stmt.setString(2, rol.getDescripcion());
            stmt.setLong(3, rol.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rol;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM rol WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM rol WHERE id = ?";
        
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
    public Optional<Rol> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM rol WHERE LOWER(nombre) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToRol(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Rol> buscarPorEstado(boolean activo) {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM rol WHERE activo = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, activo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                roles.add(mapResultSetToRol(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return roles;
    }

    @Override
    public List<Rol> buscarRolesConPermiso(String permiso) {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT DISTINCT r.* FROM rol r " +
                     "JOIN rol_permisos rp ON r.id = rp.rol_id " +
                     "JOIN permisos p ON rp.permiso_id = p.id " +
                     "WHERE LOWER(p.nombre) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + permiso + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                roles.add(mapResultSetToRol(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return roles;
    }
}
