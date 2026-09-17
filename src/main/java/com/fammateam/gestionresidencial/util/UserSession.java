package main.java.com.fammateam.gestionresidencial.util;

import main.java.com.fammateam.gestionresidencial.model.Usuario;

public class UserSession {
    
    private static UserSession instance;
    private Usuario usuarioLogueado;

    private UserSession(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public static void setSession(Usuario usuario) {
        instance = new UserSession(usuario);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public Usuario getUsuario() {
        return usuarioLogueado;
    }

    public static void cleanSession() {
        instance = null;
    }
   
    
}
