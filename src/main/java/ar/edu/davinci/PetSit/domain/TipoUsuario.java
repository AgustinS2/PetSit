package ar.edu.davinci.PetSit.domain;

import java.util.LinkedList;
import java.util.List;
public enum TipoUsuario {
	
	VETERINARIO("Veterinario"),
	REFUGIO("Refugio"),
	DUEÑO("Dueño");
	
private String descripcion;

private TipoUsuario(String descripcion) {
	this.descripcion = descripcion;
}

public String getDescripcion() {
	return descripcion;
}
public static List<TipoUsuario> getTipoUsuarios() {
	List<TipoUsuario> tipoPrendas = new LinkedList<TipoUsuario>();
	tipoPrendas.add(TipoUsuario.VETERINARIO);
	tipoPrendas.add(TipoUsuario.REFUGIO);
	tipoPrendas.add(TipoUsuario.DUEÑO);
	
	return tipoPrendas;
}
}