package main.java.com.fammateam.gestionresidencial.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Reserva;
import main.java.com.fammateam.gestionresidencial.repository.ReservaRepository;

public class ReservaService {

    private ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public ObservableList<Reserva> buscarReservasPorArea(int idArea) {
        try {
            return reservaRepository.findReservasByAreaId(idArea);
        } catch (Exception e) {
            throw new RuntimeException("Error en el servicio al buscar reservas por área: " + e.getMessage());
        }
    }

    public boolean guardarReserva(Reserva reservaExistente, int idArea, int idResidente, LocalDate fecha,
                                   LocalTime horaInicio, LocalTime horaFin, String estado) throws SQLException {

        validarHorario(horaInicio, horaFin);

        if (reservaExistente == null) {
            Reserva nuevaReserva = new Reserva(idArea, idResidente, fecha, horaInicio, horaFin, estado);
            return reservaRepository.createReserva(nuevaReserva);
        }

        Reserva reservaActualizada = new Reserva(
                reservaExistente.getIdReserva(),
                idArea,
                idResidente,
                fecha,
                horaInicio,
                horaFin,
                estado
        );
        return reservaRepository.updateReserva(reservaActualizada);
    }

    public boolean registrarReserva(Reserva reserva) throws SQLException {
        return reservaRepository.createReserva(reserva);
    }

    public boolean actualizarReserva(Reserva reserva) throws SQLException {
        return reservaRepository.updateReserva(reserva);
    }

    public boolean eliminarReserva(Reserva reserva) throws SQLException {
        return reservaRepository.deleteReserva(reserva);
    }

    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("Debe especificar la hora de inicio y de fin.");
        }
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
        }
    }
}