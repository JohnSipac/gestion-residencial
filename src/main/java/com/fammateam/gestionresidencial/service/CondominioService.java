package main.java.com.fammateam.gestionresidencial.service;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.repository.CondominioRepository;
import java.sql.SQLException;

public class CondominioService {

    private final CondominioRepository condominioRepository;

    public CondominioService(CondominioRepository condominioRepository) {
        this.condominioRepository = condominioRepository;
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
        if (condominio.getNombre().trim().isEmpty() || condominio.getDireccion().trim().isEmpty() || condominio.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("Hay campos vacíos");
        }
        return condominioRepository.updateCondominio(condominio);
    }

    public boolean eliminarCondominio(Condominio condominio) throws Exception {
        return condominioRepository.deleteCondominio(condominio);
    }

    public ObservableList<String> encontrarNombres() {
        return condominioRepository.findNames();
    }

    public Condominio obtenerCondominioPorNombre(String nombre) {
        return condominioRepository.findCondominioByName(nombre);
    }
}
