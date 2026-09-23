package main.java.com.fammateam.gestionresidencial.service;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.model.CasaResidente;
import main.java.com.fammateam.gestionresidencial.model.Residente;
import main.java.com.fammateam.gestionresidencial.repository.CasaRepository;
import main.java.com.fammateam.gestionresidencial.repository.CasaResidenteRepository;
import main.java.com.fammateam.gestionresidencial.repository.ResidenteRepository;

public class CasaResidenteService {

    private final CasaResidenteRepository casaResidenteRepository;
    private final CasaRepository casaRepository;
    private final ResidenteRepository residenteRepository;

    public CasaResidenteService(CasaResidenteRepository casaResidenteRepository, CasaRepository casaRepository, ResidenteRepository residenteRepository) {
        this.casaResidenteRepository = casaResidenteRepository;
        this.casaRepository = casaRepository;
        this.residenteRepository = residenteRepository;
    }

    public boolean registerCasaResidente(CasaResidente asignacion) throws SQLException {
        return casaResidenteRepository.registerCasaResidente(asignacion);
    }

    public ObservableList<CasaResidente> findAll() throws SQLException {
        return casaResidenteRepository.findAll();
    }

    public boolean updateCasaResidente(CasaResidente asignacion) throws SQLException {
        return casaResidenteRepository.updateCasaResidente(asignacion);
    }

    public boolean deleteCasaResidente(int idCasa, int idResidente) throws SQLException {
        return casaResidenteRepository.deleteCasaResidente(idCasa, idResidente);
    }

    public ObservableList<Casa> getCasas() throws SQLException {
        return casaRepository.ListCasas();
    }

    public ObservableList<Residente> getResidentes() throws SQLException {
        return residenteRepository.findAll();
    }
}
