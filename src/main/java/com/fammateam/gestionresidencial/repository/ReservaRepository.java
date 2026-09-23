package main.java.com.fammateam.gestionresidencial.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import main.java.com.fammateam.gestionresidencial.model.Reserva;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.SQLException;

public class ReservaRepository {

    public ObservableList<Reserva> findReservasByAreaId(int idArea) {
        String sql = "SELECT"
                + " res.id_reserva,"
                + " a.id_area,"
                + " a.nombre AS nombre_area,"
                + " r.id_residente,"
                + " r.nombre AS nombre_residente,"
                + " res.fecha_reserva,"
                + " res.hora_inicio,"
                + " res.hora_fin,"
                + " res.estado"
                + " FROM reservas res"
                + " JOIN areas_comunes a ON res.id_area = a.id_area"
                + " JOIN residentes r ON res.id_residente = r.id_residente"
                + " WHERE a.id_area = ?;";

        ObservableList<Reserva> listaReservas = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, idArea);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int idReserva = rs.getInt("id_reserva");
                    int areaId = rs.getInt("id_area");
                    String nombreArea = rs.getString("nombre_area");
                    int idResidente = rs.getInt("id_residente");
                    String nombreResidente = rs.getString("nombre_residente");
                    LocalDate fechaReserva = rs.getObject("fecha_reserva", LocalDate.class);
                    LocalTime horaInicio = rs.getObject("hora_inicio", LocalTime.class);
                    LocalTime horaFin = rs.getObject("hora_fin", LocalTime.class);
                    String estado = rs.getString("estado");

                    Reserva reserva = new Reserva(
                            idReserva,
                            areaId,
                            nombreArea,
                            idResidente,
                            nombreResidente,
                            fechaReserva,
                            horaInicio,
                            horaFin,
                            estado
                    );

                    listaReservas.add(reserva);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            throw new RuntimeException("Error al buscar reservas: " + e.getMessage());
        }

        return listaReservas;
    }

    public boolean createReserva(Reserva reserva) {
        String sql = "INSERT INTO reservas (id_area, id_residente, fecha_reserva, hora_inicio, hora_fin, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, reserva.getIdArea());
            pstm.setInt(2, reserva.getIdResidente());

            pstm.setObject(3, reserva.getFechaReserva());
            pstm.setObject(4, reserva.getHoraInicio());
            pstm.setObject(5, reserva.getHoraFin());

            pstm.setString(6, reserva.getEstado());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear reserva: " + e.getMessage());
            return false;
        }
    }

    public boolean updateReserva(Reserva reserva) {
        String sql = "update reservas set id_area = ?, id_residente = ?, fecha_reserva = ?, hora_inicio = ?, hora_fin = ?, estado = ? where id_reserva = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, reserva.getIdArea());
            pstm.setInt(2, reserva.getIdResidente());
            pstm.setObject(3, reserva.getFechaReserva());
            pstm.setObject(4, reserva.getHoraInicio());
            pstm.setObject(5, reserva.getHoraFin());
            pstm.setString(6, reserva.getEstado());
            pstm.setInt(7, reserva.getIdReserva());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar reserva: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReserva(Reserva reserva) {
        String sql = "delete from reservas where id_reserva = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, reserva.getIdReserva());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar la reserva: " + e.getMessage());
            return false;
        }
    }

}
