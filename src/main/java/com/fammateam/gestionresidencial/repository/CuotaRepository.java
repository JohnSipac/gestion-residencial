package main.java.com.fammateam.gestionresidencial.repository;

import main.java.com.fammateam.gestionresidencial.model.Cuota;
import java.sql.SQLException;
import main.java.com.fammateam.gestionresidencial.config.DataBaseConnection;
import java.sql.ResultSet;
import main.java.com.fammateam.gestionresidencial.model.enums.EstadoCuota;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CuotaRepository {

    public CuotaRepository() {
    }

    public Cuota findCuotaById(Cuota cuota) throws SQLException {
        String sql = "SELECT id_cuota, id_casa, mes, anio, monto, estado, fecha_vencimiento FROM cuotas WHERE id_cuota = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, cuota.getIdCuota());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Cuota(
                            rs.getInt("id_cuota"),
                            rs.getInt("id_casa"),
                            rs.getInt("mes"),
                            rs.getInt("anio"),
                            rs.getDouble("monto"),
                            EstadoCuota.fromString(rs.getString("estado")),
                            rs.getDate("fecha_vencimiento").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    public List<Cuota> findCuotaByCasa(Cuota cuota) throws SQLException {
        List<Cuota> lista = new ArrayList<>();
        String sql = "SELECT * FROM cuotas WHERE id_casa = ? ORDER BY anio DESC, mes DESC";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            pstm.setInt(1, cuota.getIdCasa());

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Cuota(
                            rs.getInt("id_cuota"),
                            rs.getInt("id_casa"),
                            rs.getInt("mes"),
                            rs.getInt("anio"),
                            rs.getDouble("monto"),
                            EstadoCuota.fromString(rs.getString("estado")),
                            rs.getDate("fecha_vencimiento").toLocalDate()
                    ));
                }
            }
        }
        return lista;
    }

    public ObservableList<Cuota> listCuota() throws SQLException {
        String sql = "SELECT id_cuota, id_casa, mes, anio, monto, estado, fecha_vencimiento "
                + "FROM cuotas "
                + "ORDER BY anio DESC, mes DESC";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {

            ResultSet rs = pstm.executeQuery();
            ObservableList<Cuota> listaCuotas = FXCollections.observableArrayList();

            while (rs.next()) {
                listaCuotas.add(new Cuota(
                        rs.getInt("id_cuota"),
                        rs.getInt("id_casa"),
                        rs.getInt("mes"),
                        rs.getInt("anio"),
                        rs.getDouble("monto"),
                        EstadoCuota.fromString(rs.getString("estado")),
                        rs.getDate("fecha_vencimiento").toLocalDate()
                ));
            }

            return listaCuotas;
        }
    }

    public boolean createCuota(Cuota cuota) throws SQLException {
        String sql = "INSERT INTO cuotas (id_casa, mes, anio, monto, estado, fecha_vencimiento) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, cuota.getIdCasa());
            pstm.setInt(2, cuota.getMes());
            pstm.setInt(3, cuota.getAnio());
            pstm.setDouble(4, cuota.getMonto());
            pstm.setString(5, cuota.getEstado().getDbValue());
            pstm.setDate(6, java.sql.Date.valueOf(cuota.getFechaVencimiento()));

            return pstm.executeUpdate() > 0;
        }
    }

    public boolean updateCuota(Cuota cuota) throws SQLException {
        boolean actualizado = false;
        String sql = "UPDATE cuotas SET id_casa = ?, mes = ?, anio = ?, monto = ?, estado = ?, fecha_vencimiento WHERE id_cuota = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, cuota.getIdCasa());
            pstm.setInt(2, cuota.getMes());
            pstm.setInt(3, cuota.getAnio());
            pstm.setDouble(4, cuota.getMonto());
            pstm.setString(5, cuota.getEstado().getDbValue());
            pstm.setDate(6, java.sql.Date.valueOf(cuota.getFechaVencimiento()));

            int filas = pstm.executeUpdate();

            if (filas > 0) {
                actualizado = true;
            }
            return actualizado;
        }
    }
    
        public boolean deleteCuota(Cuota cuota) throws SQLException {
        boolean eliminado = false;
        String sql = "delete from cuotas where id_cuota = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, cuota.getIdCuota());

            int filas = pstm.executeUpdate();

            if (filas > 0) {
                eliminado = true;
            }
            return eliminado;
        }
    }
}
