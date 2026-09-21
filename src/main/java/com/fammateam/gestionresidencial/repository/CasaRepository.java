package main.java.com.fammateam.gestionresidencial.repository;

import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CasaRepository {

    public CasaRepository() {
    }

    public Casa findCasaById(Casa casa) throws SQLException {
        String sql = "SELECT id_casa, id_condominio, numero_casa, aliquota FROM casas WHERE id_casa = ?";

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
                        rs.getInt("id_casa"),
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

    public boolean createCasa(Casa casa) {
        String sql = "insert into casas (id_condominio, numero_casa, aliquota) values (?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, casa.getIdCondominio());
            pstm.setString(2, casa.getNumeroCasa());
            pstm.setDouble(3, casa.getAliquota());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("La casa '" + casa.getNumeroCasa() + "' ya está registrada en este condominio.");
            }
            System.out.println("Error al crear casa: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCasa(Casa casa) throws SQLException {
        boolean actualizado = false;
        String sql = "UPDATE casas SET id_condominio = ?, numero_casa = ?, aliquota = ? WHERE id_casa = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, casa.getIdCondominio());
            pstm.setString(2, casa.getNumeroCasa());
            pstm.setDouble(3, casa.getAliquota());
            pstm.setInt(4, casa.getIdCasa());

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
        String sql = "delete from casas where id_casa = ?";

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
