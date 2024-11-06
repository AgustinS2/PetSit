package PetsitApp.service;

import PetsitApp.repository.AlertaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import PetsitApp.model.Alerta;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    public Alerta crearAlerta(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

}
