package ar.edu.davinci.PetSit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/petsit")

public abstract class PetSitApp {
	// Esto es equivalente a hacer
	// http://localhost:8090/petsit
	// El puerto 8090 está configurado en la propiedad server.port
}