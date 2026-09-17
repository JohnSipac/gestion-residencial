
package main.java.com.fammateam.gestionresidencial.repository;

import main.java.com.fammateam.gestionresidencial.model.AreaComun;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaComunRepository {

    public boolean guardar(AreaComun area) {
        String sql = "INSERT INTO areas_comunes (id_condominio, nombre, capacidad_maxima, costo_reserva) VALUES (?, ?, ?, ?)";
        
        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, area.getIdCondominio());
            pst.setString(2, area.getNombre());
            pst.setInt(3, area.getCapacidadMaxima());
            pst.setDouble(4, area.getCostoReserva());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AreaComun> buscarTodos() {
        List<AreaComun> lista = new ArrayList<>();
        String sql = "SELECT * FROM areas_comunes";
        try (Connection con = DataBaseConnection.getConnectionDataBase(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                AreaComun a = new AreaComun();
                a.setIdArea(rs.getInt("id_area"));
                a.setIdCondominio(rs.getInt("id_condominio"));
                a.setNombre(rs.getString("nombre"));
                a.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
                a.setCostoReserva(rs.getDouble("costo_reserva"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizar(AreaComun area) {
        String sql = "UPDATE areas_comunes SET nombre = ?, capacidad_maxima = ?, costo_reserva = ? WHERE id_area = ?";
        try (Connection con = DataBaseConnection.getConnectionDataBase(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, area.getNombre());
            pst.setInt(2, area.getCapacidadMaxima());
            pst.setDouble(3, area.getCostoReserva());
            pst.setInt(4, area.getIdArea());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idArea) {
        String sql = "DELETE FROM areas_comunes WHERE id_area = ?";
        try (Connection con = DataBaseConnection.getConnectionDataBase(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, idArea);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}