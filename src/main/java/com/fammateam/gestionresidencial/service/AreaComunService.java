package main.java.com.fammateam.gestionresidencial.service;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.AreaComun;
import main.java.com.fammateam.gestionresidencial.repository.AreaComunRepository;
import java.sql.SQLException;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.repository.CondominioRepository;

public class AreaComunService {

    private final AreaComunRepository areaComunRepository;
    private final CondominioRepository condominioRepository;

    public AreaComunService(AreaComunRepository areaComunRepository, CondominioRepository condominioRepository) {
        this.areaComunRepository = areaComunRepository;
        this.condominioRepository = condominioRepository;
    }

    public ObservableList<AreaComun> getAllAreasComunes() throws SQLException {
        return areaComunRepository.findAllWithCondominio();
    }

    public boolean saveAreaComun(AreaComun area) throws SQLException {
        return areaComunRepository.registerAreaComun(area);
    }

    public boolean updateAreaComun(AreaComun area) throws SQLException {
        return areaComunRepository.updateAreaComun(area);
    }

    public boolean removeAreaComun(int idArea) throws SQLException {
        return areaComunRepository.deleteAreaComun(idArea);
    }

    public ObservableList<Condominio> getAllCondominios() throws SQLException {
        return condominioRepository.findAll();
    }
}
