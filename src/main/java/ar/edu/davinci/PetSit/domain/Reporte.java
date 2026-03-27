package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reportes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rep_id")
    private Long id;

    // Ahora es OPCIONAL estar logeado para reportar(para permitir reportes sin usuario)
    @ManyToOne
    @JoinColumn(name = "rep_usuario_id", nullable = true)
    private Usuario usuario;

    @Column(name = "rep_fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "rep_ubicacion")
    private String ubicacion;

    @Column(name = "rep_descripcion")
    private String descripcion;

    @Column(name = "rep_foto")
    private String foto;

    // 🔹 Estado interno del sistema (pendiente, aprobado, etc.)
    @Column(name = "rep_estado")
    private String estado;

    // 🔹 NUEVO: tipo de reporte (define color)
    @Enumerated(EnumType.STRING)
    @Column(name = "rep_tipo_reporte", nullable = false)
    private TipoReporte tipoReporte;

    // 🔹 NUEVO: estado real de la mascota
    @Enumerated(EnumType.STRING)
    @Column(name = "rep_estado_mascota", nullable = false)
    private EstadoMascota estadoMascota;

    // 🔹 NUEVO: coordenadas para el mapa
    @Column(name = "rep_lat")
    private Double lat;

    @Column(name = "rep_lng")
    private Double lng;

    @ManyToOne
    @JoinColumn(name = "rep_refugio_id")
    private Refugio refugio;

    @ManyToOne
    @JoinColumn(name = "rep_alerta_id")
    private Alerta alerta;

    // 🔹 Antes de guardar en DB
    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }

        if (estado == null) {
            estado = "PENDIENTE";
        }

        // 🔹 Lógica automática según tipo de reporte
        actualizarEstadoSegunTipo();
    }

    // 🔹 Define estado según tipo
    public void actualizarEstadoSegunTipo() {
        if (tipoReporte == null) return;

        if (tipoReporte == TipoReporte.TENGO_CONMIGO) {
            estadoMascota = EstadoMascota.ENCONTRADO;
        } else {
            estadoMascota = EstadoMascota.PERDIDO;
        }
    }

    // Color de las alertas para el mapa (para el front)
    public String getColorMapa() {
        if (tipoReporte == null) return "gray";

        return switch (tipoReporte) {
            case MASCOTA_PERDIDA -> "red";
            case SOLO_LO_VI -> "yellow";
            case TENGO_CONMIGO -> "green";
        };
    }
}