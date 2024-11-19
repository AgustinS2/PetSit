package ar.edu.davinci.PetSit.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/petsit/api")

public abstract class PetSitRest {
	// Esto es equivalente a hacer
	// http://localhost:8090/petsit/api
	// El puerto 8090, está configurado en la propiedad server.port

}