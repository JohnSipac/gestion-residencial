package main.java.com.fammateam.gestionresidencial.service;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.repository.CasaRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class CasaService {

    private CasaRepository casaRepository;

    public CasaService(CasaRepository casaRepository) {
        this.casaRepository = casaRepository;
    }

    public ObservableList<Casa> listaCasa() {
        try {
            return casaRepository.ListCasas();
        } catch (SQLException e) {
            throw new RuntimeException("Error en el servicio al listar casa: " + e.getMessage());
        }
    }
    
    public List<Casa> findCasasByCondominio(Casa casa) {
        try {
            return casaRepository.findCasasByCondominio(casa);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean registrarCasa(Casa casa) throws SQLException {
        return casaRepository.createCasa(casa);
    }

    public boolean actualizarCasa(Casa casa) throws SQLException {
        return casaRepository.updateCasa(casa);
    }

    public boolean eliminarCasa(Casa casa) throws SQLException {
        return casaRepository.deleteCasa(casa);
    }
}