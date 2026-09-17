package main.java.com.fammateam.gestionresidencial.service;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.repository.CondominioRepository;
import java.sql.SQLException;

public class CondominioService {
    
    private final CondominioRepository condominioRepository;

    public CondominioService() {
        this.condominioRepository = new CondominioRepository();
    }

    public ObservableList<Condominio> listarCondominios() {
        try {
            return condominioRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error en el servicio al listar condominios: " + e.getMessage());
        }
    }

    public boolean registrarCondominio(Condominio condominio) throws Exception {
        
        if (condominio.getNombre().trim().isEmpty() || condominio.getDireccion().trim().isEmpty() || condominio.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("Hay campos vacíos");
        }
        
        return condominioRepository.createCondominio(condominio);
    }

    public boolean modificarCondominio(Condominio condominio) throws Exception {
        if (condominio.getIdCondominio() <= 0) {
            throw new IllegalArgumentException("ID de condominio inválido para actualizar.");
        }
        return condominioRepository.updateCondominio(condominio);
    }

    public boolean eliminarCondominio(Condominio condominio) throws Exception {
        if (condominio == null || condominio.getIdCondominio() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un condominio válido para eliminar.");
        }
        return condominioRepository.deleteCondominio(condominio);
    }
    
}
