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

    public ObservableList<Casa> findCasasByCondominioName(String nombreCondominio) {
        String sql = "select c.id_casa, c.id_condominio, c.numero_casa, c.aliquota "
                + "from casas c "
                + "inner join condominios co on c.id_condominio = co.id_condominio "
                + "where co.nombre = ?";

        ObservableList<Casa> listaCasas = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, nombreCondominio);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    listaCasas.add(new Casa(
                            rs.getInt("id_casa"),
                            rs.getInt("id_condominio"),
                            rs.getString("numero_casa"),
                            rs.getDouble("aliquota")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al rastrear casas: " + e.getMessage());
        }
        return listaCasas;
    }

    public ObservableList<Casa> findAll() {
        String sql = "select * from casas";
        ObservableList<Casa> listaCasas = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                listaCasas.add(new Casa(
                        rs.getInt("id_casa"),
                        rs.getInt("id_condominio"),
                        rs.getString("numero_casa"),
                        rs.getDouble("aliquota")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al rastrear casas: " + e.getMessage());
        }
        return listaCasas;
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

    public boolean updateCasa(Casa casa) {
        String sql = "update casas set id_condominio = ?, numero_casa = ?, aliquota = ? where id_casa = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, casa.getIdCondominio());
            pstm.setString(2, casa.getNumeroCasa());
            pstm.setDouble(3, casa.getAliquota());
            pstm.setInt(4, casa.getIdCasa());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("La casa '" + casa.getNumeroCasa() + "' ya está registrada en este condominio.");
            }
            System.out.println("Error al actualizar casa: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCasa(Casa casa) {
    String sql = "delete from casas where id_casa = ?";

    try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
        pstm.setInt(1, casa.getIdCasa());

        return pstm.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al eliminar la casa: " + e.getMessage());
        return false;
    }
}
}
