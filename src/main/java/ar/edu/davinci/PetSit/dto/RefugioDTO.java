package ar.edu.davinci.PetSit.dto;

import ar.edu.davinci.PetSit.domain.Refugio;

/** DTO de salida para Refugio. Coincide con model/Refugio.java (Android). */
public class RefugioDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String foto;
    private String ubicacion;
    private Double lat;
    private Double lng;

    public RefugioDTO() {
    }

    public RefugioDTO(Refugio refugio) {
        this.id = refugio.getId();
        this.nombre = refugio.getNombre();
        this.direccion = refugio.getDireccion();
        this.telefono = refugio.getTelefono();
        this.correo = refugio.getCorreo();
        this.foto = refugio.getFoto();
        this.ubicacion = refugio.getUbicacion();
        this.lat = refugio.getLat();
        this.lng = refugio.getLng();
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
