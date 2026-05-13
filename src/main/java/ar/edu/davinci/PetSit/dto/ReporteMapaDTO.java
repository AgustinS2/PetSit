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

    private Long    id;
    private Double  lat;
    private Double  lng;
    private String  ubicacion;
    private String  descripcion;
    private String  foto;
    private String  estado;          // PENDIENTE / RESUELTO
    private String  tipoReporte;     // MASCOTA_PERDIDA / SOLO_LO_VI / TENGO_CONMIGO
    private String  estadoMascota;   // PERDIDO / ENCONTRADO
    private String  color;           // red / yellow / green

    // Datos de la mascota
    private String  nombreMascota;
    private String  especie;
    private String  raza;
    private String  tamano;

    // Datos de contacto del reportante
    private String  contactoTel;     // teléfono del usuario que reportó
    private String  rolReportante;   // DUENO / ENCONTRADOR
    private String  zona;            // barrio / zona
    private String  fecha;           // fecha formateada dd/MM/yyyy
}
