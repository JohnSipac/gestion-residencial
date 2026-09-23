package main.java.com.fammateam.gestionresidencial.config;

public class Credentials {

    public static final String URL_DB = System.getenv("DB_URL") + "/gestion_residencial_in4bm";
    public static final String USER_DB = System.getenv("DB_USER");
    public static final String PASS_DB = System.getenv("DB_PASS");

}
