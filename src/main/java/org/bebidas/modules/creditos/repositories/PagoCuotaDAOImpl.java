package org.bebidas.modules.creditos.repositories;

import org.bebidas.core.util.GenericDAOImpl;
import org.bebidas.infraestructure.conexion.DatabaseConnection;
import org.bebidas.modules.creditos.PagoCuota;
import org.bebidas.modules.pagos.repostiories.PagoCuotaDAO;

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

public class PagoCuotaDAOImpl extends GenericDAOImpl<PagoCuota, Long> implements PagoCuotaDAO {

    public PagoCuotaDAOImpl() {
        super(PagoCuota.class);
    }

    private PagoCuota mapResultSetToPagoCuota(ResultSet rs) throws SQLException {
        PagoCuota pagoCuota = new PagoCuota();
        pagoCuota.setId(rs.getLong("id"));
        pagoCuota.setCreditoId(rs.getLong("credito_id"));
        pagoCuota.setFechaPago(rs.getDate("fecha_pago") != null ? rs.getDate("fecha_pago").toLocalDate() : null);
        pagoCuota.setMonto(rs.getBigDecimal("monto"));
        pagoCuota.setMetodo(rs.getString("metodo"));
        pagoCuota.setNroTransaccion(rs.getString("nro_transaccion"));
        pagoCuota.setObservacion(rs.getString("observacion"));
        pagoCuota.setNumeroCuota(rs.getInt("numero_cuota"));
        pagoCuota.setPagoId(rs.getLong("pago_id"));
        return pagoCuota;
    }

    @Override
    public Optional<PagoCuota> findById(Long id) {
        String sql = "SELECT * FROM pagos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPagoCuota(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<PagoCuota> findAll() {
        List<PagoCuota> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pagos ORDER BY fecha_pago DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pagos.add(mapResultSetToPagoCuota(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pagos;
    }

    @Override
    public PagoCuota save(PagoCuota pagoCuota) {
        if (pagoCuota.getId() == null) {
            return insert(pagoCuota);
        } else {
            return update(pagoCuota);
        }
    }

    private PagoCuota insert(PagoCuota pagoCuota) {
        String sql = "INSERT INTO pagos (credito_id, fecha_pago, monto, metodo, nro_transaccion, observacion, numero_cuota, pago_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, pagoCuota.getCreditoId());
            stmt.setDate(2, pagoCuota.getFechaPago() != null ? Date.valueOf(pagoCuota.getFechaPago()) : null);
            stmt.setBigDecimal(3, pagoCuota.getMonto());
            stmt.setString(4, pagoCuota.getMetodo());
            stmt.setString(5, pagoCuota.getNroTransaccion());
            stmt.setString(6, pagoCuota.getObservacion());
            stmt.setInt(7, pagoCuota.getNumeroCuota());
            stmt.setLong(8, pagoCuota.getPagoId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pagoCuota.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pagoCuota;
    }

    private PagoCuota update(PagoCuota pagoCuota) {
        String sql = "UPDATE pagos SET credito_id = ?, fecha_pago = ?, monto = ?, metodo = ?, nro_transaccion = ?, observacion = ?, numero_cuota = ?, pago_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, pagoCuota.getCreditoId());
            stmt.setDate(2, pagoCuota.getFechaPago() != null ? Date.valueOf(pagoCuota.getFechaPago()) : null);
            stmt.setBigDecimal(3, pagoCuota.getMonto());
            stmt.setString(4, pagoCuota.getMetodo());
            stmt.setString(5, pagoCuota.getNroTransaccion());
            stmt.setString(6, pagoCuota.getObservacion());
            stmt.setInt(7, pagoCuota.getNumeroCuota());
            stmt.setLong(8, pagoCuota.getPagoId());
            stmt.setLong(9, pagoCuota.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pagoCuota;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM pagos WHERE id = ?";

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
        String sql = "SELECT 1 FROM pagos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}