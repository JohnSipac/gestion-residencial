package main.java.com.fammateam.gestionresidencial.model;

public class AreaComun {

    private int idArea;
    private int idCondominio;
    private String nombre;
    private int capacidadMaxima;
    private double costoReserva;
    private String nombreCondominio;

    public AreaComun(int idArea, int idCondominio, String nombre, int capacidadMaxima, double costoReserva, String nombreCondominio) {
        this.idArea = idArea;
        this.idCondominio = idCondominio;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.costoReserva = costoReserva;
        this.nombreCondominio = nombreCondominio;
    }

    public AreaComun(int idCondominio, String nombre, int capacidadMaxima, double costoReserva) {
        this.idCondominio = idCondominio;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.costoReserva = costoReserva;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public int getIdCondominio() {
        return idCondominio;
    }

    public void setIdCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public double getCostoReserva() {
        return costoReserva;
    }

    public void setCostoReserva(double costoReserva) {
        this.costoReserva = costoReserva;
    }

    public String getNombreCondominio() {
        return nombreCondominio;
    }

    public void setNombreCondominio(String nombreCondominio) {
        this.nombreCondominio = nombreCondominio;
    }

}
