package ar.edu.davinci.PetSit.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ar.edu.davinci.PetSit.controller.PetSitApp;

@Controller
public class HomeController extends PetSitApp {
	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	@GetMapping({"/" , "/index"})
	public String viewHomePage(Model model) {
	LOGGER.info("GET - viewHomePage - /index");
	return "index";
	}
	
	  @GetMapping("/index2")
	    public String index2() {
	        return "index2"; // Renderiza templates/index2.html
	    }
}