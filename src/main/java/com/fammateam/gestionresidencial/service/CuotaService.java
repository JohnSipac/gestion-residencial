package main.java.com.fammateam.gestionresidencial.service;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.model.Cuota;
import main.java.com.fammateam.gestionresidencial.repository.CuotaRepository;

public class CuotaService {

    public CuotaRepository cuotaRepository;

    public CuotaService(CuotaRepository cuotaRepository) {
        this.cuotaRepository = cuotaRepository;
    }

    public Cuota findCuotaById(Cuota cuota) throws SQLException {
        return cuotaRepository.findCuotaById(cuota);
    }

    public List<Cuota> findCuotaByCasa(Casa casa) throws SQLException {
        return cuotaRepository.findCuotaByCasa(casa);
    }

    public ObservableList<Cuota> listaCuota() throws SQLException {
        return cuotaRepository.listCuota();
    }

    public boolean crearCuota(Cuota cuota) throws SQLException {
        if (cuota.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto de la cuota no puede ser 0 o negativo");
        }
        try {
            return cuotaRepository.createCuota(cuota);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Ya existe una cuota para este mes de este año en esta casa.");
            }
            throw e;
        }
    }

    public boolean actualizarCuota(Cuota cuota) throws SQLException {

        if (cuota.getIdCuota() <= 0) {
            throw new IllegalArgumentException("El monto que quiere actualizar no puede ser 0 o negativo.");
        }

        Cuota cuotaSelect = cuotaRepository.findCuotaById(cuota);
        if (cuotaSelect == null) {
            throw new IllegalArgumentException("La cuota que intenta actualizar no existe.");
        }
        return cuotaRepository.updateCuota(cuota);
    }

    public boolean eliminarCuota(Cuota cuota) throws SQLException {
        Cuota cuotaSelect = cuotaRepository.findCuotaById(cuota);
        if (cuotaSelect == null) {
            throw new IllegalArgumentException("La cuota que quiere eliminar no existe.");
        }
        return cuotaRepository.deleteCuota(cuota);
    }

    public ObservableList<Cuota> listaCuotaConNumeroCasa() throws SQLException {
        return cuotaRepository.listCuotaConNumeroCasa();
    }

}
