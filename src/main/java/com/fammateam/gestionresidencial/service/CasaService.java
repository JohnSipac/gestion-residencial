package main.java.com.fammateam.gestionresidencial.service;

import java.util.List;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.repository.CasaRepository;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CasaService {
    
    private CasaRepository casaRepository;
    
    public CasaService(CasaRepository casaRepository){
    this.casaRepository = casaRepository;
    }
    
    public List<Casa> obtenerCasas() throws SQLException{
    return casaRepository.ListCasas();
    }
    
    public boolean registrarCasa(Casa casa)throws SQLException{ 
      return casaRepository.createCasa(casa);
    }
}
