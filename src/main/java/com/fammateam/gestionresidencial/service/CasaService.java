package main.java.com.fammateam.gestionresidencial.service;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.repository.CasaRepository;

public class CasaService {

    private final CasaRepository casaRepository;

    public CasaService(CasaRepository casaRepository) {
        this.casaRepository = casaRepository;
    }

    public ObservableList<Casa> listarCasas() {
        return casaRepository.findAll();
    }

    public ObservableList<Casa> listarCasasPorCondominio(String nombreCondominio) {
        return casaRepository.findCasasByCondominioName(nombreCondominio);
    }

    public boolean registrarCasa(Casa casa) {
        return casaRepository.createCasa(casa);
    }

    public boolean modificarCasa(Casa casa) {
        return casaRepository.updateCasa(casa);
    }

    public boolean eliminarCasa(Casa casa) {
        return casaRepository.deleteCasa(casa);
    }
}