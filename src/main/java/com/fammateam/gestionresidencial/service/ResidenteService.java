package main.java.com.fammateam.gestionresidencial.service;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Residente;
import main.java.com.fammateam.gestionresidencial.repository.ResidenteRepository;
import java.sql.SQLException;

public class ResidenteService {
    
    private ResidenteRepository residenteRepository;

    public ResidenteService(ResidenteRepository residenteRepository) {
        this.residenteRepository = residenteRepository;
    }

    public ObservableList<Residente> listarResidentes() {
        try {
            return residenteRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error en el servicio al listar residentes: " + e.getMessage());
        }
    }

    public boolean registrarResidente(Residente residente) throws Exception {        
        if (residente.getNombre().trim().isEmpty() || residente.getApellido().trim().isEmpty() || residente.getEmail().trim().isEmpty() || residente.getTelefono().trim().isEmpty() || residente.getTipoResidente().trim().isEmpty()) {
            throw new IllegalArgumentException("Hay campos vacíos");
        }      
        return residenteRepository.createResidente(residente);
    }

    public boolean modificarResidente(Residente residente) throws Exception {
        if (residente.getNombre().trim().isEmpty() || residente.getApellido().trim().isEmpty() || residente.getEmail().trim().isEmpty() || residente.getTelefono().trim().isEmpty() || residente.getTipoResidente().trim().isEmpty()) {
            throw new IllegalArgumentException("Hay campos vacíos");
        }
        return residenteRepository.updateResidente(residente);
    }

    public boolean eliminarResidente(Residente residente) throws Exception {
        return residenteRepository.deleteResidente(residente);
    }
    
}
