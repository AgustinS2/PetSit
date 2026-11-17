package ar.edu.davinci.PetSit.dto;

/** Body que manda la app para postularse a una adopción. */
public class CrearPostulacionRequestDTO {

    private Long adopcionId;
    private String mensaje;

    public Long getAdopcionId() {
        return adopcionId;
    }

    public void setAdopcionId(Long adopcionId) {
        this.adopcionId = adopcionId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
