package main.java.com.fammateam.gestionresidencial.repository;

import main.java.com.fammateam.gestionresidencial.model.AreaComun;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;

public class AreaComunRepository {

    public boolean registerAreaComun(AreaComun area) throws SQLException {
        String sql = "INSERT INTO areas_comunes (id_condominio, nombre, capacidad_maxima, costo_reserva) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, area.getIdCondominio());
            pstm.setString(2, area.getNombre());
            pstm.setInt(3, area.getCapacidadMaxima());
            pstm.setDouble(4, area.getCostoReserva());

            return pstm.executeUpdate() > 0;
        }
    }

    public ObservableList<AreaComun> findAllWithCondominio() throws SQLException {
        ObservableList<AreaComun> areas = FXCollections.observableArrayList();
        String sql = "SELECT a.id_area, a.id_condominio, a.nombre, a.capacidad_maxima, a.costo_reserva, c.nombre AS nombre_condominio "
                + "FROM areas_comunes a "
                + "INNER JOIN condominios c ON a.id_condominio = c.id_condominio";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                areas.add(new AreaComun(
                        rs.getInt("id_area"),
                        rs.getInt("id_condominio"),
                        rs.getString("nombre"),
                        rs.getInt("capacidad_maxima"),
                        rs.getDouble("costo_reserva"),
                        rs.getString("nombre_condominio")
                ));
            }
        }
        return areas;
    }

    public AreaComun findAreaById(int idArea) throws SQLException {
        String sql = "SELECT a.id_area, a.id_condominio, a.nombre, a.capacidad_maxima, a.costo_reserva, c.nombre AS nombre_condominio "
                + "FROM areas_comunes a "
                + "INNER JOIN condominios c ON a.id_condominio = c.id_condominio "
                + "WHERE a.id_area = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, idArea);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new AreaComun(
                            rs.getInt("id_area"),
                            rs.getInt("id_condominio"),
                            rs.getString("nombre"),
                            rs.getInt("capacidad_maxima"),
                            rs.getDouble("costo_reserva"),
                            rs.getString("nombre_condominio")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateAreaComun(AreaComun area) throws SQLException {
        String sql = "UPDATE areas_comunes SET id_condominio = ?, nombre = ?, capacidad_maxima = ?, costo_reserva = ? "
                + "WHERE id_area = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, area.getIdCondominio());
            pstm.setString(2, area.getNombre());
            pstm.setInt(3, area.getCapacidadMaxima());
            pstm.setDouble(4, area.getCostoReserva());
            pstm.setInt(5, area.getIdArea());

            return pstm.executeUpdate() > 0;
        }
    }

    public boolean deleteAreaComun(int idArea) throws SQLException {
        String sql = "DELETE FROM areas_comunes WHERE id_area = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, idArea);

            return pstm.executeUpdate() > 0;
        }
    }

}
