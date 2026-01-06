package ar.edu.davinci.PetSit.dto;

import lombok.Data;

@Data
public class RegisterForm {
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String telefono;
}

