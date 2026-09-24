package main.java.com.fammateam.gestionresidencial.model;

import java.time.LocalDate;
import main.java.com.fammateam.gestionresidencial.model.enums.EstadoCuota;

public class Cuota {

    private int idCuota;
    private int idCasa;
    private int mes;
    private int anio;
    private double monto;
    private EstadoCuota estado;
    private LocalDate fechaVencimiento;
    private String numeroCasa;

    public Cuota(int idCuota, int idCasa, int mes, int anio, double monto, EstadoCuota estado, LocalDate fechaVencimiento) {
        this.idCuota = idCuota;
        this.idCasa = idCasa;
        this.mes = mes;
        this.anio = anio;
        this.monto = monto;
        this.estado = estado;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Cuota(int idCuota, int idCasa, String numeroCasa) {
        this.idCuota = idCuota;
        this.idCasa = idCasa;
        this.numeroCasa = numeroCasa;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public EstadoCuota getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuota estado) {
        this.estado = estado;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }
    
    
}
