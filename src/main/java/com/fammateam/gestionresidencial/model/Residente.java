package main.java.com.fammateam.gestionresidencial.model;

public class Residente {
    
    private int idResidente;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String tipoResidente;

    public Residente(int idResidente, String nombre, String apellido, String email, String telefono, String tipoResidente) {
        this.idResidente = idResidente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.tipoResidente = tipoResidente;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoResidente() {
        return tipoResidente;
    }

    public void setTipoResidente(String tipoResidente) {
        this.tipoResidente = tipoResidente;
    }
    
    
    
}
