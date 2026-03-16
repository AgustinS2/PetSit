package ar.edu.davinci.PetSit.controller.web.Alerta;

import ar.edu.davinci.PetSit.domain.Alerta;
import ar.edu.davinci.PetSit.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/petsit")
@RequiredArgsConstructor
public class AlertaMapaController {


    private final AlertaRepository alertaRepository;

    // AlertaMapaController.java
    @GetMapping("/alertas/mapa")
    public String verMapa(Model model) {
        // List<Alerta> list = alertaRepository.findByLatIsNotNullAndLngIsNotNull();

        // var puntos = list.stream().map(a -> Map.<String, Object>of(
        //         "lat", a.getLat(),                       // ya no nulos por el findBy...
        //        "lng", a.getLng(),
        //       "descripcion", a.getDescripcion() != null ? a.getDescripcion() : "",
        //        "ubicacion",   a.getUbicacion()   != null ? a.getUbicacion()   : "",
        //        "mascota",     a.getMascota() != null && a.getMascota().getNombre() != null
        //                ? a.getMascota().getNombre()
        //                : "Mascota"
        // )).toList();

        //model.addAttribute("alertas", puntos);
        return "alertas/mapa";
    }
}
