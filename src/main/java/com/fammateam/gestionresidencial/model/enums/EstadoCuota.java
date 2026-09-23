package main.java.com.fammateam.gestionresidencial.model.enums;

public enum EstadoCuota {
    PENDIENTE("Pendiente"),
    PAGADO("Pagado"),
    ATRASADO("Atrasado");   
    
    private final String dbValue;

    EstadoCuota(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static EstadoCuota fromString(String text) {
        if (text != null) {
            for (EstadoCuota estado : EstadoCuota.values()) {
                if (estado.dbValue.equalsIgnoreCase(text.trim())) {
                    return estado;
                }
            }
        }
        return PENDIENTE;
    }

    @Override
    public String toString() {
        switch (this) {
            case PENDIENTE: return "Pendiente";
            case PAGADO: return "Pagado";
            case ATRASADO: return "Atrasado";
            default: return super.toString();
        }
    }
}
