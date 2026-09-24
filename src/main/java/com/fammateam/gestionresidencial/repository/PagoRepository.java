package main.java.com.fammateam.gestionresidencial.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import main.java.com.fammateam.gestionresidencial.model.Pago;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PagoRepository {

    public PagoRepository() {
    }

    public ObservableList<Pago> findPagosByCondominioId(int idCondominio) throws SQLException {
        String sql = "SELECT"
                + " p.id_pago,"
                + " p.id_cuota,"
                + " p.monto_pagado,"
                + " p.fecha_pago,"
                + " p.metodo_pago,"
                + " co.id_condominio,"
                + " co.nombre AS nombre_condominio,"
                + " ca.id_casa,"
                + " ca.numero_casa"
                + " FROM pagos p"
                + " JOIN cuotas c ON p.id_cuota = c.id_cuota"
                + " JOIN casas ca ON c.id_casa = ca.id_casa"
                + " JOIN condominios co ON ca.id_condominio = co.id_condominio"
                + " WHERE co.id_condominio = ?;";

        ObservableList<Pago> listaPagos = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, idCondominio);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int idPago = rs.getInt("id_pago");
                    int idCuota = rs.getInt("id_cuota");
                    java.math.BigDecimal montoPagado = rs.getBigDecimal("monto_pagado");
                    LocalDateTime fechaPago = rs.getObject("fecha_pago", LocalDateTime.class);
                    String metodoPago = rs.getString("metodo_pago");
                    int condominioId = rs.getInt("id_condominio");
                    String nombreCondominio = rs.getString("nombre_condominio");
                    int idCasa = rs.getInt("id_casa");
                    String numeroCasa = rs.getString("numero_casa");

                    Pago pago = new Pago(
                            idPago,
                            idCuota,
                            montoPagado,
                            fechaPago,
                            metodoPago,
                            condominioId,
                            nombreCondominio,
                            idCasa,
                            numeroCasa
                    );

                    listaPagos.add(pago);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pagos: " + e.getMessage(), e);
        }

        return listaPagos;
    }

    public boolean createPago(Pago pago) throws SQLException {
        String sql = "INSERT INTO pagos (id_cuota, monto_pagado, fecha_pago, metodo_pago) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, pago.getIdCuota());
            pstm.setBigDecimal(2, pago.getMontoPagado());
            pstm.setObject(3, pago.getFechaPago());
            pstm.setString(4, pago.getMetodoPago());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Ya existe un pago registrado para esta cuota.");
            }
            throw e;
        }
    }

    public boolean updatePago(Pago pago) throws SQLException {
        String sql = "update pagos set id_cuota = ?, monto_pagado = ?, fecha_pago = ?, metodo_pago = ? where id_pago = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, pago.getIdCuota());
            pstm.setBigDecimal(2, pago.getMontoPagado());
            pstm.setObject(3, pago.getFechaPago());
            pstm.setString(4, pago.getMetodoPago());
            pstm.setInt(5, pago.getIdPago());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Ya existe un pago registrado para esta cuota.");
            }
            throw e;
        }
    }

    public boolean deletePago(Pago pago) throws SQLException {
        String sql = "delete from pagos where id_pago = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, pago.getIdPago());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            throw e;
        }
    }
}
