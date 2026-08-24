package ar.edu.davinci.PetSit.dto;

import ar.edu.davinci.PetSit.domain.Adopcion;

/** DTO de salida para Adopcion. Coincide con model/Adopcion.java (Android). */
public class AdopcionDTO {

    private Long id;
    private Long mascotaId;
    private String mascotaNombre;
    private String mascotaFoto;
    private Long usuarioId;
    private String usuarioNombre;
    private String fechaPublicacion;
    private String estado;
    private String descripcion;
    private String foto;
    private String ubicacion;
    private Long refugioId;
    private String refugioNombre;

    public AdopcionDTO() {
    }

    public AdopcionDTO(Adopcion adopcion) {
        this.id = adopcion.getId();
        if (adopcion.getMascota() != null) {
            this.mascotaId = adopcion.getMascota().getId();
            this.mascotaNombre = adopcion.getMascota().getNombre();
            this.mascotaFoto = adopcion.getMascota().getFoto();
        }
        if (adopcion.getUsuario() != null) {
            this.usuarioId = adopcion.getUsuario().getId();
            this.usuarioNombre = adopcion.getUsuario().getNombre();
        }
        if (adopcion.getFechaPublicacion() != null) {
            this.fechaPublicacion = adopcion.getFechaPublicacion().toString();
        }
        this.estado = adopcion.getEstado();
        this.descripcion = adopcion.getDescripcion();
        this.foto = adopcion.getFoto();
        this.ubicacion = adopcion.getUbicacion();
        if (adopcion.getRefugio() != null) {
            this.refugioId = adopcion.getRefugio().getId();
            this.refugioNombre = adopcion.getRefugio().getNombre();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
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

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Long getRefugioId() {
        return refugioId;
    }

    public void setRefugioId(Long refugioId) {
        this.refugioId = refugioId;
    }

    public String getRefugioNombre() {
        return refugioNombre;
    }

    public void setRefugioNombre(String refugioNombre) {
        this.refugioNombre = refugioNombre;
    }
}
