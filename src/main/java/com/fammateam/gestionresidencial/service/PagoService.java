package main.java.com.fammateam.gestionresidencial.service;

import javafx.collections.ObservableList;
import main.java.com.fammateam.gestionresidencial.model.Pago;
import main.java.com.fammateam.gestionresidencial.repository.PagoRepository;
import java.sql.SQLException;

public class PagoService {

    private PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public ObservableList<Pago> listarPagosPorCondominio(int idCondominio) throws SQLException {
        return pagoRepository.findPagosByCondominioId(idCondominio);
    }

    public boolean registrarPago(Pago pago) throws SQLException {
        if (pago.getMontoPagado() == null || pago.getMontoPagado().signum() <= 0
                || pago.getFechaPago() == null
                || pago.getMetodoPago() == null || pago.getMetodoPago().trim().isEmpty()) {
            throw new IllegalArgumentException("Hay campos vacíos o inválidos");
        }
        return pagoRepository.createPago(pago);
    }

    public boolean modificarPago(Pago pago) throws SQLException {
        if (pago.getMontoPagado() == null || pago.getMontoPagado().signum() <= 0
                || pago.getFechaPago() == null
                || pago.getMetodoPago() == null || pago.getMetodoPago().trim().isEmpty()) {
            throw new IllegalArgumentException("Hay campos vacíos o inválidos");
        }
        return pagoRepository.updatePago(pago);
    }

    public boolean eliminarPago(Pago pago) throws SQLException {
        return pagoRepository.deletePago(pago);
    }
}