package org.bebidas.modules.proveedor.repositories;

import org.bebidas.core.util.GenericDAOImpl;
import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.proveedor.Proveedor;
import org.bebidas.modules.usuarios.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProveedorDAOImpl extends GenericDAOImpl<Proveedor, Long> implements ProveedorDAO {

    public ProveedorDAOImpl() {
        super(Proveedor.class);
    }

    private Proveedor mapResultSetToProveedor(ResultSet rs) throws SQLException {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(rs.getLong("id"));
        proveedor.setNombre(rs.getString("nombre"));
        proveedor.setTelefono(rs.getString("telefono"));
        proveedor.setNit(rs.getString("nit"));
        proveedor.setCorreo(rs.getString("correo"));
        proveedor.setDireccion(rs.getString("direccion"));
    
        return proveedor;
    }

    @Override
    public Optional<Proveedor> findById(Long id) {
        String sql = "SELECT * FROM proveedor WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToProveedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Proveedor> findAll() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                proveedores.add(mapResultSetToProveedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return proveedores;
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        if (proveedor.getId() == null) {
            return insert(proveedor);
        } else {
            return update(proveedor);
        }
    }

    private Proveedor insert(Proveedor proveedor) {
        String sql = "INSERT INTO proveedor (nombre, telefono, nit, correo, direccion) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTelefono());
            stmt.setString(3, proveedor.getNit());
            stmt.setString(4, proveedor.getCorreo());
            stmt.setString(5, proveedor.getDireccion());
 
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        proveedor.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return proveedor;
    }

    private Proveedor update(Proveedor proveedor) {
        String sql = "UPDATE proveedor SET nombre = ?, telefono = ?, nit = ?, correo = ?, direccion = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTelefono());
            stmt.setString(3, proveedor.getNit());
            stmt.setString(4, proveedor.getCorreo());
            stmt.setString(5, proveedor.getDireccion());
            stmt.setLong(6, proveedor.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return proveedor;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM proveedor WHERE id = ?";
        
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
        String sql = "SELECT COUNT(*) FROM proveedore WHERE id = ?";
        
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
    public List<Proveedor> buscarPorNombre(String nombre) {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedore WHERE LOWER(nombre) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                proveedores.add(mapResultSetToProveedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return proveedores;
    }

    @Override
    public List<Proveedor> buscarPorRuc(String ruc) {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedore WHERE nit = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ruc);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                proveedores.add(mapResultSetToProveedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return proveedores;
    }

    

    @Override
    public List<Proveedor> buscarPorTipoProducto(String tipoProducto) {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM proveedore p " +
                     "JOIN productos prod ON p.id = prod.proveedor_id " +
                     "WHERE LOWER(prod.tipo) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + tipoProducto + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                proveedores.add(mapResultSetToProveedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return proveedores;
    }
}
