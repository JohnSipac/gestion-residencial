package main.java.com.fammateam.gestionresidencial.repository;

import main.java.com.fammateam.gestionresidencial.model.Cuota;
import java.sql.SQLException;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import java.sql.ResultSet;
import main.java.com.fammateam.gestionresidencial.model.enums.EstadoCuota;
import java.sql.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Casa;

public class CuotaRepository {

    public CuotaRepository() {
    }

    public Cuota findCuotaById(Cuota cuota) throws SQLException {
        String sql = "SELECT id_cuota, id_casa, mes, anio, monto, estado, fecha_vencimiento FROM cuotas WHERE id_cuota = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, cuota.getIdCuota());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Cuota(
                            rs.getInt("id_cuota"),
                            rs.getInt("id_casa"),
                            rs.getInt("mes"),
                            rs.getInt("anio"),
                            rs.getDouble("monto"),
                            EstadoCuota.fromString(rs.getString("estado")),
                            rs.getDate("fecha_vencimiento").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

}
