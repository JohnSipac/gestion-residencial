package main.java.com.fammateam.gestionresidencial.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private int idReserva;
    private int idArea;
    private int idResidente;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private String nombreArea;
    private String nombreResidente;

    public Reserva(int idReserva, int idArea, int idResidente, LocalDate fechaReserva, LocalTime horaInicio, LocalTime horaFin, String estado) {
        this.idReserva = idReserva;
        this.idArea = idArea;
        this.idResidente = idResidente;
        this.fechaReserva = fechaReserva;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    public Reserva(int idReserva, int idArea, String nombreArea, int idResidente, String nombreResidente, LocalDate fechaReserva, LocalTime horaInicio, LocalTime horaFin, String estado) {
        this.idReserva = idReserva;
        this.idArea = idArea;
        this.nombreArea = nombreArea;
        this.idResidente = idResidente;
        this.nombreResidente = nombreResidente;
        this.fechaReserva = fechaReserva;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }
    
    public Reserva(int idArea, int idResidente, LocalDate fechaReserva, LocalTime horaInicio, LocalTime horaFin, String estado) {
        this.idArea = idArea;
        this.idResidente = idResidente;
        this.fechaReserva = fechaReserva;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

}
