package ar.edu.davinci.PetSit.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.davinci.PetSit.controller.PetSitApp;

@Controller
@RequestMapping("/petsit/")
public class HomeController extends PetSitApp {
	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	@GetMapping({"/" , "/index"})
	public String viewHomePage(Model model) {
	LOGGER.info("GET - viewHomePage - /index");
	return "index";
	}
	
	@GetMapping("/home/registro")
	    public String registro() {
	        return "home/registro";
	    }
	@GetMapping("/home/login")
	    public String login() {
	        return "home/login"; // Renderiza templates/login.html
	    }
	@GetMapping("/home/nosotros")
	    public String nosotros() {
	        return "home/nosotros"; // Renderiza templates/home/nosotros.html
	    }
	@GetMapping("/home/contacto")
	    public String contacto() {
	        return "home/contacto"; // Renderiza templates/home/contacto.html
	    }

    @GetMapping("/home/recuperarpass")
	    public String recuperarpass() {
	        return "home/recuperarpass"; // Renderiza templates/"""".html
	}

}