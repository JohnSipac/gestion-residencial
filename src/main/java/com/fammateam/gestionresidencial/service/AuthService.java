package main.java.com.fammateam.gestionresidencial.service;

import java.sql.SQLException;
import main.java.com.fammateam.gestionresidencial.model.Usuario;
import main.java.com.fammateam.gestionresidencial.repository.UsuarioRepository;
import main.java.com.fammateam.gestionresidencial.security.jbcrypt.BCrypt;
import main.java.com.fammateam.gestionresidencial.util.UserSession;


public class AuthService {
    
    private UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    

    public boolean autenticator(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        try {
            Usuario usuario = usuarioRepository.findUserbyUsername(username.trim());

            if (usuario != null && BCrypt.checkpw(password, usuario.getPasswordHash())) {
                UserSession.setSession(usuario);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión durante el login: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean registerUser(Usuario nuevoUsuario, String plainPassword) throws SQLException {
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        nuevoUsuario.setPasswordHash(hashedPassword);
        return usuarioRepository.registerUsuario(nuevoUsuario);
    }
    
}
