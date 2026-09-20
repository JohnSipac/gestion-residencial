package main.java.com.fammateam.gestionresidencial.repository;

import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CasaRepository {

    public CasaRepository() {
    }

    public Casa findCasaById(Casa casa) throws SQLException {
        String sql = "SELECT id_casa, id_domicilio, numero_casa, aliquota FROM casa WHERE id_casa = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, casa.getIdCasa());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Casa(
                            rs.getInt("id_casa"),
                            rs.getInt("id_condominio"),
                            rs.getString("numero_casa"),
                            rs.getDouble("aliquota")
                    );
                }
            }
        }
        return null;
    }

    public ObservableList<Casa> ListCasas() throws SQLException {

        String sql = "SELECT c.id_casa, c.id_condominio, c.numero_casa, c.aliquota, con.nombre AS nombre_condominio "
                + "FROM casas c "
                + "INNER JOIN condominios con ON c.id_condominio = con.id_condominio "
                + "ORDER BY c.id_casa ASC";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            ResultSet rs = pstm.executeQuery();
            ObservableList<Casa> listaCasas = FXCollections.observableArrayList();

            while (rs.next()) {
                listaCasas.add(new Casa(
                        rs.getInt("id_casas"),
                        rs.getInt("id_condominio"),
                        rs.getString("numero_casa"),
                        rs.getDouble("aliquota"))
                );

            }
            return listaCasas;

        } catch (SQLException e) {
            throw new RuntimeException("Error al rastrear casa: " + e.getMessage());
        }
    }

    public boolean createCasa(Casa casa) throws SQLException {
        boolean creado = false;
        String sql = "insert into casas values (?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, casa.getIdCondominio());
            pstm.setString(2, casa.getNumeroCasa());
            pstm.setDouble(3, casa.getAliquota());

            int filas = pstm.executeUpdate();

            if (filas > 0) {
                creado = true;
            }
            return creado;

        } catch (Exception e) {
            System.out.println("Error al crear casa:" + e.getMessage());
            return creado;
        }

    }

    public boolean updateCasa(Casa casa) throws SQLException {
        boolean actualizado = false;
        String sql = "update casas id_condominio = ?, numero_casa = ?, aliquota = ? where id_casa = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, casa.getIdCondominio());
            pstm.setString(2, casa.getNumeroCasa());
            pstm.setDouble(3, casa.getAliquota());

            int filas = pstm.executeUpdate();

            if (filas > 0) {
                actualizado = true;
            }
            return actualizado;

        } catch (Exception e) {
            System.out.println("Error al actualizar casa: " + e.getMessage());
            return actualizado;
        }

    }
    
    public boolean deleteCasa(Casa casa) throws SQLException {
        boolean eliminado = false;
        String sql = "delete from casas where id_casas = ?";
 
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, casa.getIdCasa());
 
            int filas = pstm.executeUpdate();
 
            if (filas > 0) {
                eliminado = true;
            }
            return eliminado;
 
        } catch (Exception e) {
            System.out.println("Error al eliminar la casa: " + e.getMessage());
            return eliminado;
        }
 
    }
}
