
package main.java.com.fammateam.gestionresidencial.model;

import java.sql.Date;

public class CasaResidente {
    private int idCasa;
    private int idResidente;
    private Date fechaInicio;

    public CasaResidente() {}

    public CasaResidente(int idCasa, int idResidente, Date fechaInicio) {
        this.idCasa = idCasa;
        this.idResidente = idResidente;
        this.fechaInicio = fechaInicio;
    }

    public int getIdCasa() { 
        return idCasa; 
    }
    
    public void setIdCasa(int idCasa) { 
        this.idCasa = idCasa;
    }
    
    public int getIdResidente() { 
        return idResidente; 
    }
    
    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente; 
    }
    
    public Date getFechaInicio() { 
        return fechaInicio;
    }
    
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio; 
    }
}
