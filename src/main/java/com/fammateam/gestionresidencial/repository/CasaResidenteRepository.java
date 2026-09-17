
package main.java.com.fammateam.gestionresidencial.repository;

import main.java.com.fammateam.gestionresidencial.model.CasaResidente;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CasaResidenteRepository {

    public boolean guardar(CasaResidente cr) {
        String sql = "INSERT INTO casa_residente (id_casa, id_residente, fecha_inicio) VALUES (?, ?, ?)";
        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, cr.getIdCasa());
            pst.setInt(2, cr.getIdResidente());
            pst.setDate(3, cr.getFechaInicio());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CasaResidente> buscarTodos() {
        List<CasaResidente> lista = new ArrayList<>();
        String sql = "SELECT * FROM casa_residente";
        try (Connection con = DataBaseConnection.getConnectionDataBase(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                CasaResidente cr = new CasaResidente();
                cr.setIdCasa(rs.getInt("id_casa"));
                cr.setIdResidente(rs.getInt("id_residente"));
                cr.setFechaInicio(rs.getDate("fecha_inicio"));
                lista.add(cr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizar(int idCasaAntiguo, int idResidenteAntiguo, CasaResidente nuevo) {
        String sql = "UPDATE casa_residente SET id_casa = ?, id_residente = ?, fecha_inicio = ? WHERE id_casa = ? AND id_residente = ?";
        try (Connection con = DataBaseConnection.getConnectionDataBase(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, nuevo.getIdCasa());
            pst.setInt(2, nuevo.getIdResidente());
            pst.setDate(3, nuevo.getFechaInicio());
            pst.setInt(4, idCasaAntiguo);
            pst.setInt(5, idResidenteAntiguo);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idCasa, int idResidente) {
        String sql = "DELETE FROM casa_residente WHERE id_casa = ? AND id_residente = ?";
        try (Connection con = DataBaseConnection.getConnectionDataBase(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, idCasa);
            pst.setInt(2, idResidente);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
