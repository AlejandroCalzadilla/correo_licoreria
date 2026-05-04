package org.bebidas.modules.inventario.repositories;

import org.bebidas.core.util.GenericDAOImpl;
import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.inventario.repositories.interfaces.InventarioDAO;
import org.bebidas.modules.usuarios.Usuario;

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

public class InventarioDAOImpl extends GenericDAOImpl<Inventario, Long> implements InventarioDAO {

    public InventarioDAOImpl() {
        super(Inventario.class);
    }

    private Inventario mapResultSetToInventario(ResultSet rs) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.setId(rs.getLong("id"));
        inventario.setTipoMovimiento(rs.getString("tipo_movimiento"));
        inventario.setCantidad(rs.getInt("cantidad"));
        inventario.setFecha(rs.getDate("fecha").toLocalDate());
        inventario.setStockActual(rs.getInt("stock_actual"));
        inventario.setGlosa(rs.getString("glosa"));

        // Usuario relationship
        Long usuarioId = rs.getLong("usuario_id");
        if (rs.wasNull()) {
            usuarioId = null;
        }
        if (usuarioId != null) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            inventario.setUsuario(usuario);
        }

        // Producto relationship
        Long productoId = rs.getLong("producto_id");
        if (rs.wasNull()) {
            productoId = null;
        }
        if (productoId != null) {
            Producto producto = new Producto();
            producto.setId(productoId);
            inventario.setProducto(producto);
        }

        // Note: detalleCompra and detalleVenta relationships not mapped as they might
        // be optional

        return inventario;
    }

    @Override
    public Optional<Inventario> findById(Long id) {
        String sql = "SELECT * FROM inventario WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToInventario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Inventario> findAll() {
        List<Inventario> inventarios = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                inventarios.add(mapResultSetToInventario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventarios;
    }

    @Override
    public Inventario save(Inventario inventario) {
        if (inventario.getId() == null) {
            return insert(inventario);
        } else {
            return update(inventario);
        }
    }

    private Inventario insert(Inventario inventario) {
        System.out.println("esta llegando al repo");
        String sql = "INSERT INTO inventario (tipo_movimiento, cantidad, fecha, stock_actual, glosa, usuario_id, producto_id, detalle_compra_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, inventario.getTipoMovimiento());
            stmt.setInt(2, inventario.getCantidad());
            stmt.setDate(3, Date.valueOf(inventario.getFecha()));
            stmt.setInt(4, inventario.getStockActual());
            stmt.setString(5, inventario.getGlosa());
            if (inventario.getUsuario() != null) {
                stmt.setLong(6, inventario.getUsuario().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }
            if (inventario.getProducto() != null) {
                stmt.setLong(7, inventario.getProducto().getId());
            } else {
                stmt.setNull(7, java.sql.Types.BIGINT);
            }
            if (inventario.getDetalleCompra().getId() != null) {
                stmt.setLong(8, inventario.getDetalleCompra().getId());
            } else {
                stmt.setNull(8, java.sql.Types.BIGINT);
            }

            int affectedRows = stmt.executeUpdate();
            System.out.println("Filas afectadas al insertar inventario: " + affectedRows);
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inventario.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventario;
    }

    private Inventario update(Inventario inventario) {
        String sql = "UPDATE inventario SET tipo_movimiento = ?, cantidad = ?, fecha = ?, stock_actual = ?, glosa = ?, usuario_id = ?, producto_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inventario.getTipoMovimiento());
            stmt.setInt(2, inventario.getCantidad());
            stmt.setDate(3, Date.valueOf(inventario.getFecha()));
            stmt.setInt(4, inventario.getStockActual());
            stmt.setString(5, inventario.getGlosa());
            if (inventario.getUsuario() != null) {
                stmt.setLong(6, inventario.getUsuario().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }
            if (inventario.getProducto() != null) {
                stmt.setLong(7, inventario.getProducto().getId());
            } else {
                stmt.setNull(7, java.sql.Types.BIGINT);
            }
            stmt.setLong(8, inventario.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventario;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM inventario WHERE id = ?";

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
        String sql = "SELECT COUNT(*) FROM inventario WHERE id = ?";

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
    public List<Inventario> buscarPorProducto(Long productoId) {
        List<Inventario> inventarios = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE producto_id = ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, productoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                inventarios.add(mapResultSetToInventario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventarios;
    }

    @Override
    public List<Inventario> buscarPorTipoMovimiento(String tipoMovimiento) {
        List<Inventario> inventarios = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE tipo_movimiento = ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoMovimiento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                inventarios.add(mapResultSetToInventario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventarios;
    }

    @Override
    public List<Inventario> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        List<Inventario> inventarios = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fin));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                inventarios.add(mapResultSetToInventario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventarios;
    }

    @Override
    public List<Inventario> buscarPorUsuario(Long usuarioId) {
        List<Inventario> inventarios = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE usuario_id = ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                inventarios.add(mapResultSetToInventario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventarios;
    }

    @Override
    public Integer obtenerStockActual(Long productoId) {
        // Obtener el último movimiento por fecha y ID para asegurar el más reciente
        String sql = "SELECT stock_actual FROM inventario WHERE producto_id = ? ORDER BY fecha DESC, id DESC LIMIT 1";

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

        return 0; // Si no hay movimientos, stock inicial es 0
    }

    /**
     * Obtiene el último movimiento de inventario para un producto específico
     * 
     * @param productoId ID del producto
     * @return Optional con el último movimiento, vacío si no existe
     */
    public Optional<Inventario> obtenerUltimoMovimiento(Long productoId) {
        String sql = "SELECT * FROM inventario WHERE producto_id = ? ORDER BY fecha DESC, id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToInventario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
