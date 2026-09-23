    package main.java.com.fammateam.gestionresidencial.model;

    public class CasaResidente {

        private int idCasa;
        private int idResidente;
        private String fechaInicio;

        public CasaResidente(int idCasa, int idResidente, String fechaInicio) {
            this.idCasa = idCasa;
            this.idResidente = idResidente;
            this.fechaInicio = fechaInicio;
        }

        public CasaResidente() {
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

        public String getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(String fechaInicio) {
            this.fechaInicio = fechaInicio;
        }



    }
