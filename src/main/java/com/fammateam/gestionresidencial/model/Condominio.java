package main.java.com.fammateam.gestionresidencial.model;

public class Condominio {

    private int idCondominio;
    private String nombre;
    private String direccion;
    private String telefono;

    public Condominio(int idCondominio, String nombre, String direccion, String telefono) {
        this.idCondominio = idCondominio;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Condominio() {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
    
}
