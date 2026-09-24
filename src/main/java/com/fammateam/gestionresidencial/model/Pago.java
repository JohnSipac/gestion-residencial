package main.java.com.fammateam.gestionresidencial.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {
    
    private int idPago;
    private int idCuota;
    private BigDecimal montoPagado;
    private LocalDateTime fechaPago;
    private String metodoPago;
    private int idCondominio;
    private String nombreCondominio;
    private int idCasa;
    private String numeroCasa;

    public Pago(int idPago, int idCuota, BigDecimal montoPagado, LocalDateTime fechaPago, String metodoPago, int idCondominio, String nombreCondominio, int idCasa, String numeroCasa) {
        this.idPago = idPago;
        this.idCuota = idCuota;
        this.montoPagado = montoPagado;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
        this.idCondominio = idCondominio;
        this.nombreCondominio = nombreCondominio;
        this.idCasa = idCasa;
        this.numeroCasa = numeroCasa;
    }

    public Pago(int idCuota, BigDecimal montoPagado, LocalDateTime fechaPago, String metodoPago) {
        this.idCuota = idCuota;
        this.montoPagado = montoPagado;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getIdCondominio() {
        return idCondominio;
    }

    public void setIdCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }

    public String getNombreCondominio() {
        return nombreCondominio;
    }

    public void setNombreCondominio(String nombreCondominio) {
        this.nombreCondominio = nombreCondominio;
    }

    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }
    
    
}
