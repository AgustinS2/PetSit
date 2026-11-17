package ar.edu.davinci.PetSit.dto;

import ar.edu.davinci.PetSit.domain.Postulacion;

/** DTO de salida para Postulacion. Coincide con model/Postulacion.java (Android). */
public class PostulacionDTO {

    private Long id;
    private Long adopcionId;
    private String mascotaNombre;
    private String mascotaFoto;
    private Long publicadorId;
    private String publicadorNombre;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioCorreo;
    private String usuarioTelefono;
    private String fecha;
    private String mensaje;

    public PostulacionDTO() {
    }

    public PostulacionDTO(Postulacion postulacion) {
        this.id = postulacion.getId();
        if (postulacion.getAdopcion() != null) {
            this.adopcionId = postulacion.getAdopcion().getId();
            if (postulacion.getAdopcion().getMascota() != null) {
                this.mascotaNombre = postulacion.getAdopcion().getMascota().getNombre();
                this.mascotaFoto = postulacion.getAdopcion().getMascota().getFoto();
            }
            if (postulacion.getAdopcion().getUsuario() != null) {
                this.publicadorId = postulacion.getAdopcion().getUsuario().getId();
                this.publicadorNombre = postulacion.getAdopcion().getUsuario().getNombre();
            }
        }
        if (postulacion.getUsuario() != null) {
            this.usuarioId = postulacion.getUsuario().getId();
            this.usuarioNombre = postulacion.getUsuario().getNombre();
            this.usuarioCorreo = postulacion.getUsuario().getCorreo();
            this.usuarioTelefono = postulacion.getUsuario().getTelefono();
        }
        if (postulacion.getFecha() != null) {
            this.fecha = postulacion.getFecha().toString();
        }
        this.mensaje = postulacion.getMensaje();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdopcionId() {
        return adopcionId;
    }

    public void setAdopcionId(Long adopcionId) {
        this.adopcionId = adopcionId;
    }

    public String getMascotaNombre() {
        return mascotaNombre;
    }

    public void setMascotaNombre(String mascotaNombre) {
        this.mascotaNombre = mascotaNombre;
    }

    public String getMascotaFoto() {
        return mascotaFoto;
    }

    public void setMascotaFoto(String mascotaFoto) {
        this.mascotaFoto = mascotaFoto;
    }

    public Long getPublicadorId() {
        return publicadorId;
    }

    public void setPublicadorId(Long publicadorId) {
        this.publicadorId = publicadorId;
    }

    public String getPublicadorNombre() {
        return publicadorNombre;
    }

    public void setPublicadorNombre(String publicadorNombre) {
        this.publicadorNombre = publicadorNombre;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioCorreo() {
        return usuarioCorreo;
    }

    public void setUsuarioCorreo(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    public String getUsuarioTelefono() {
        return usuarioTelefono;
    }

    public void setUsuarioTelefono(String usuarioTelefono) {
        this.usuarioTelefono = usuarioTelefono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
