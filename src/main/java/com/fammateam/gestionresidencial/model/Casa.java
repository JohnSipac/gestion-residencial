package main.java.com.fammateam.gestionresidencial.model;

public class Casa {
    
    private int idCasa;
    private int idCondominio;
    private String numeroCasa;
    private double aliquota;

    public Casa(int idCasa, int idCondominio, String numeroCasa, double aliquota) {
        this.idCasa = idCasa;
        this.idCondominio = idCondominio;
        this.numeroCasa = numeroCasa;
        this.aliquota = aliquota;
    }
    
    public Casa(){
    }
    
    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    public int getIdCondominio() {
        return idCondominio;
    }

    public void setIdCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }
    
    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public double getAliquota() {
        return aliquota;
    }

    public void setAliquota(double aliquota) {
        this.aliquota = aliquota;
    }
}
