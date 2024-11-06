package PetsitApp.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import PetsitApp.model.Mascota;
import PetsitApp.repository.MascotaRepository;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    public Mascota registrarMascota(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

}
