package main.java.com.fammateam.gestionresidencial.repository;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Residente;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;

public class ResidenteRepository {

    public ResidenteRepository() {
    }

    public ObservableList<Residente> findAll() throws SQLException {
        String sql = "select * from residentes;";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            ObservableList<Residente> listaResidentes = FXCollections.observableArrayList();

            while (rs.next()) {
                listaResidentes.add(new Residente(
                        rs.getInt("id_residente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("tipo_residente")
                ));
            }
            return listaResidentes;
        } catch (SQLException e) {
            throw new RuntimeException("Error al rastrear residentes: " + e.getMessage());
        }
    }

    public boolean createResidente(Residente residente) {
        String sql = "insert into residentes (nombre, apellido, email, telefono, tipo_residente) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, residente.getNombre());
            pstm.setString(2, residente.getApellido());
            pstm.setString(3, residente.getEmail());
            pstm.setString(4, residente.getTelefono());
            pstm.setString(5, residente.getTipoResidente());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Ya existe el correo: " + residente.getEmail());
            }
            System.out.println("Error al crear residente: " + e.getMessage());
            return false;
        }
    }

    public boolean updateResidente(Residente residente) {
        String sql = "update residentes set nombre = ?, apellido = ?, email = ?, telefono = ?, tipo_residente = ? where id_residente = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, residente.getNombre());
            pstm.setString(2, residente.getApellido());
            pstm.setString(3, residente.getEmail());
            pstm.setString(4, residente.getTelefono());
            pstm.setString(5, residente.getTipoResidente());
            pstm.setInt(6, residente.getIdResidente());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Ya existe un residente registrado con el correo: " + residente.getEmail());
            }
            System.out.println("Error al actualizar residente: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteResidente(Residente residente) {
        String sql = "delete from residentes where id_residente = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, residente.getIdResidente());

            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar residente: " + e.getMessage());
            return false;
        }
    }

}
