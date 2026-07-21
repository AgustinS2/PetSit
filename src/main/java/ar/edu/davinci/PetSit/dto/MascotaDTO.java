package ar.edu.davinci.PetSit.dto;

import ar.edu.davinci.PetSit.domain.Mascota;

/**
 * DTO de salida para Mascota. Coincide con model/Mascota.java del lado
 */
public class MascotaDTO {

    private Long id;
    private String nombre;
    private String raza;
    private String color;
    private String tamano;
    private String descripcion;
    private String foto;
    private String sexo;
    private Long duenoId;
    private String duenoNombre;

    public MascotaDTO() {
    }

    public MascotaDTO(Mascota mascota) {
        this.id = mascota.getId();
        this.nombre = mascota.getNombre();
        this.raza = mascota.getRaza();
        this.color = mascota.getColor();
        this.tamano = mascota.getTamano();
        this.descripcion = mascota.getDescripcion();
        this.foto = mascota.getFoto();
        this.sexo = mascota.getSexo();
        if (mascota.getDueno() != null) {
            this.duenoId = mascota.getDueno().getId();
            this.duenoNombre = mascota.getDueno().getNombre();
        }
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

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Long getDuenoId() {
        return duenoId;
    }

    public void setDuenoId(Long duenoId) {
        this.duenoId = duenoId;
    }

    public String getDuenoNombre() {
        return duenoNombre;
    }

    public void setDuenoNombre(String duenoNombre) {
        this.duenoNombre = duenoNombre;
    }
}
