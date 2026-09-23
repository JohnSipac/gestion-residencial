package main.java.com.fammateam.gestionresidencial.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import main.java.com.fammateam.gestionresidencial.model.CasaResidente;

public class CasaResidenteRepository {

    public boolean registerCasaResidente(CasaResidente asignacion) throws SQLException {
        String sql = "INSERT INTO casa_residente (id_casa, id_residente, fecha_inicio) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, asignacion.getIdCasa());
            pstm.setInt(2, asignacion.getIdResidente());
            pstm.setString(3, asignacion.getFechaInicio());

            return pstm.executeUpdate() > 0;
        }
    }

    public ObservableList<CasaResidente> findAll() throws SQLException {
        ObservableList<CasaResidente> asignaciones = FXCollections.observableArrayList();
        String sql = "SELECT cr.id_casa, cr.id_residente, cr.fecha_inicio "
                + "FROM casa_residente cr "
                + "INNER JOIN casas c ON cr.id_casa = c.id_casa "
                + "INNER JOIN residentes r ON cr.id_residente = r.id_residente";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql); 
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                asignaciones.add(new CasaResidente(
                        rs.getInt("id_casa"),
                        rs.getInt("id_residente"),
                        rs.getString("fecha_inicio")
                ));
            }
        }
        return asignaciones;
    }

    public boolean updateCasaResidente(CasaResidente asignacion) throws SQLException {
        String sql = "UPDATE casa_residente SET fecha_inicio = ? "
                + "WHERE id_casa = ? AND id_residente = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setString(1, asignacion.getFechaInicio());
            pstm.setInt(2, asignacion.getIdCasa());
            pstm.setInt(3, asignacion.getIdResidente());

            return pstm.executeUpdate() > 0;
        }
    }

    public boolean deleteCasaResidente(int idCasa, int idResidente) throws SQLException {
        String sql = "DELETE FROM casa_residente WHERE id_casa = ? AND id_residente = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, idCasa);
            pstm.setInt(2, idResidente);

            return pstm.executeUpdate() > 0;
        }
    }
}