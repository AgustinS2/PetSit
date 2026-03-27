package ar.edu.davinci.PetSit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteMapaDTO {

    private Long id;
    private Double lat;
    private Double lng;
    private String ubicacion;
    private String descripcion;
    private String foto;
    private String estado;
    private String tipoReporte;
    private String estadoMascota;
    private String color;
}