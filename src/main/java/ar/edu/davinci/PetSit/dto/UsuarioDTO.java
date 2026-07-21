package ar.edu.davinci.PetSit.dto;

import ar.edu.davinci.PetSit.domain.Usuario;

/**
 * DTO de salida para Usuario.
 * la app Android solo necesita estos: (ver model/Usuario.java
 * del lado Android — tiene que tener LITERALMEENTE estos nombres de campo).
 */
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String tipo;
    private String telefono;
    private String fotoPerfil;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.correo = usuario.getCorreo();
        this.tipo = usuario.getTipo() != null ? usuario.getTipo().name() : null;
        this.telefono = usuario.getTelefono();
        this.fotoPerfil = usuario.getFotoPerfil();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
