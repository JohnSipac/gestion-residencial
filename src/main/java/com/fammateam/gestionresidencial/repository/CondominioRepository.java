package main.java.com.fammateam.gestionresidencial.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CondominioRepository {

    public CondominioRepository() {
    }

    public ObservableList<Condominio> findAll() throws SQLException {
        String sql = "select * from condominios;";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            ObservableList<Condominio> condominiumList = FXCollections.observableArrayList();

            while (rs.next()) {
                condominiumList.add(new Condominio(
                        rs.getInt("id_condominio"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                ));
            }
            return condominiumList;
        } catch (SQLException e) {
            throw new RuntimeException("Error al rastrear condominios: " + e.getMessage());
        }
    }

    public boolean createCondominio(Condominio condominio) throws Exception {
        String sql = "insert into condominios (nombre, direccion, telefono) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, condominio.getNombre());
            pstm.setString(2, condominio.getDireccion());
            pstm.setString(3, condominio.getTelefono());

            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al crear condominio: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCondominio(Condominio condominio) throws Exception {
        String sql = "update condominios set nombre = ?, direccion = ?, telefono = ? where id_condominio = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, condominio.getNombre());
            pstm.setString(2, condominio.getDireccion());
            pstm.setString(3, condominio.getTelefono());
            pstm.setInt(4, condominio.getIdCondominio());

            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al actualizar condominio: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCondominio(Condominio condominio) throws Exception {
        String sql = "delete from condominios where id_condominio = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, condominio.getIdCondominio());

            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al eliminar condominio: " + e.getMessage());
            return false;
        }
    }
}