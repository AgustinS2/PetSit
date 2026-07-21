package ar.edu.davinci.PetSit.dto;

import ar.edu.davinci.PetSit.domain.Veterinaria;

/** DTO de salida para Veterinaria. Coincide con model/Veterinaria.java (Android). */
public class VeterinariaDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String horarioAtencion;
    private String ubicacion;
    private String foto;
    private Double lat;
    private Double lng;

    public VeterinariaDTO() {
    }

    public VeterinariaDTO(Veterinaria veterinaria) {
        this.id = veterinaria.getId();
        this.nombre = veterinaria.getNombre();
        this.direccion = veterinaria.getDireccion();
        this.telefono = veterinaria.getTelefono();
        this.horarioAtencion = veterinaria.getHorarioAtencion();
        this.ubicacion = veterinaria.getUbicacion();
        this.foto = veterinaria.getFoto();
        this.lat = veterinaria.getLat();
        this.lng = veterinaria.getLng();
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

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
