package ar.edu.davinci.PetSit.dto;

/** Body de POST /petsit/api/auth/login. Coincide con LoginRequest.java (Android). */
public class LoginRequestDTO {

    private String correo;
    private String contrasena;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
