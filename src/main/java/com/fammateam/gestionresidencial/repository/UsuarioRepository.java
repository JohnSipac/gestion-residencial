package main.java.com.fammateam.gestionresidencial.repository;


import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import main.java.com.fammateam.gestionresidencial.model.Usuario;


public class UsuarioRepository {

    public boolean registerUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, username, password_hash, rol) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setString(1, usuario.getNombre());
            pstm.setString(2, usuario.getApellido());
            pstm.setString(3, usuario.getUsername());
            pstm.setString(4, usuario.getPasswordHash());
            pstm.setString(5, usuario.getRol());

            return pstm.executeUpdate() > 0;
        }
    }

    public Usuario findUserbyUsername(String username) throws SQLException {
        String sql = "SELECT id_usuario, nombre, apellido, username, password_hash, rol "
                + "FROM usuarios WHERE username = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setString(1, username);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("rol")
                    );
                }
            }
        }
        return null;
    }
}
