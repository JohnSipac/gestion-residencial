package main.java.com.fammateam.gestionresidencial.repository;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import java.sql.PreparedStatement;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
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
        boolean creado = false;
        String sql = "insert into condominios values (?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, condominio.getNombre());
            pstm.setString(2, condominio.getDireccion());
            pstm.setString(3, condominio.getTelefono());

            int filas = pstm.executeUpdate();

            if (filas > 0) {
                creado = true;
            }
            return creado;

        } catch (Exception e) {
            System.out.println("Error al crear condominio:" + e.getMessage());
            return creado;
        }

    }

    public boolean updateCondominio(Condominio condominio) throws Exception {
        boolean actualizado = false;
        String sql = "update condominios nombre = ?, direccion = ?, telefono = ? where id_condominio = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, condominio.getNombre());
            pstm.setString(2, condominio.getDireccion());
            pstm.setString(3, condominio.getTelefono());
            pstm.setInt(4, condominio.getIdCondominio());

            int filas = pstm.executeUpdate();

            if (filas > 0) {
                actualizado = true;
            }
            return actualizado;

        } catch (Exception e) {
            System.out.println("Error al actualizar condominio: " + e.getMessage());
            return actualizado;
        }

    }

    public boolean deleteCondominio(Condominio condominio) throws Exception {
        boolean eliminado = false;
        String sql = "delete from estudiantes where id_condominio = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, condominio.getIdCondominio());

            int filas = pstm.executeUpdate();

            if (filas > 0) {
                eliminado = true;
            }
            return eliminado;

        } catch (Exception e) {
            System.out.println("Error al eliminar condominio: " + e.getMessage());
            return eliminado;
        }

    }
}
