package org.bebidas.modules.dao.impl;

import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.compras.Compra;
import org.bebidas.modules.dao.interfaces.CompraDAO;
import org.bebidas.modules.model.Proveedor;
import org.bebidas.modules.usuarios.Usuario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CompraDAOImpl extends GenericDAOImpl<Compra, Long> implements CompraDAO {

    public CompraDAOImpl() {
        super(Compra.class);
    }

    private Compra mapResultSetToCompra(ResultSet rs) throws SQLException {
        Compra compra = new Compra();
        compra.setId(rs.getLong("id"));
        compra.setNroCompra(rs.getString("nro_compra"));
        compra.setFecha(LocalDate.from(rs.getTimestamp("fecha").toInstant()));
        
        compra.setEstado(rs.getString("estado"));

        // Proveedor relationship
        Long proveedorId = rs.getLong("proveedor_id");
        if (rs.wasNull()) {
            proveedorId = null;
        }
        if (proveedorId != null) {
            Proveedor proveedor = new Proveedor();
            proveedor.setId(proveedorId);
            compra.setProveedor(proveedor);
        }
        return compra;
    }

    @Override
    public Optional<Compra> findById(Long id) {
        String sql = "SELECT * FROM compra WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Compra> findAll() {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT * FROM compra ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                compras.add(mapResultSetToCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compras;
    }

    @Override
    public Compra save(Compra compra) {
        if (compra.getId() == null) {
            return insert(compra);
        } else {
            return update(compra);
        }
    }

    private Compra insert(Compra compra) {
        String sql = "INSERT INTO compra (nro_compra, fecha, estado, proveedor_id, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, compra.getNroCompra());
            stmt.setTimestamp(2, Timestamp.valueOf(compra.getFecha().atStartOfDay()));
           
            stmt.setString(3, compra.getEstado());

            if (compra.getProveedor() != null) {
                stmt.setLong(4, compra.getProveedor().getId());
            } else {
                stmt.setNull(4, java.sql.Types.BIGINT);
            }
            stmt.setString(5, compra.getDescripcion());
           

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        compra.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compra;
    }

    private Compra update(Compra compra) {
        String sql = "UPDATE compra SET nro_compra = ?, fecha = ?, estado = ?, proveedor_id = ?, descripcion = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, compra.getNroCompra());
            stmt.setTimestamp(2, Timestamp.valueOf(compra.getFecha().atStartOfDay()));
          
            stmt.setString(3, compra.getEstado());

            if (compra.getProveedor() != null) {
                stmt.setLong(4, compra.getProveedor().getId());
            } else {
                stmt.setNull(4, java.sql.Types.BIGINT);
            }
           
            stmt.setString(5, compra.getDescripcion());
           stmt.setLong(6, compra.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compra;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM compra WHERE id = ?";

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
        String sql = "SELECT COUNT(*) FROM compra WHERE id = ?";

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
    public List<Compra> buscarPorProveedor(Long proveedorId) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT * FROM compra WHERE proveedor_id = ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, proveedorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                compras.add(mapResultSetToCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compras;
    }

    

    @Override
    public List<Compra> buscarPorRangoFechas(Date fechaInicio, Date fechaFin) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT * FROM compra WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(fechaFin.getTime()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                compras.add(mapResultSetToCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compras;
    }

    @Override
    public List<Compra> buscarPorEstado(String estado) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT * FROM compra WHERE estado = ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                compras.add(mapResultSetToCompra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compras;
    }
}