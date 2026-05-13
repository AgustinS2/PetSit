package ar.edu.davinci.PetSit.controller.web;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/petsit")
public class HomeController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired private RefugioService     refugioService;
    @Autowired private VeterinariaService veterinariaService;

    @GetMapping({"/", "/index"})
    public String viewHomePage(Model model) {
        // Pasamos contadores para el hero del index
        model.addAttribute("totalRefugios",    refugioService.count());
        model.addAttribute("totalVeterinarias", veterinariaService.count());
        return "index";
    }

    @GetMapping("/home/registro")
    public String showRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "home/registro";
    }

    @GetMapping("/home/login")
    public String login() { return "home/login"; }

    @GetMapping("/home/nosotros")
    public String nosotros() { return "home/nosotros"; }

    @GetMapping("/home/contacto")
    public String contacto() { return "home/contacto"; }

    @GetMapping("/home/recuperarpass")
    public String recuperarpass() { return "home/recuperarpass"; }
}
