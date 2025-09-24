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

    @ManyToOne
    @JoinColumn(name = "rep_usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "rep_fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "rep_ubicacion")
    private String ubicacion;

    @Column(name = "rep_descripcion")
    private String descripcion;

    @Column(name = "rep_foto")
    private String foto;

    @Column(name = "rep_estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "rep_refugio_id")
    private Refugio refugio;

    @ManyToOne
    @JoinColumn(name = "rep_alerta_id")
    private Alerta alerta;

    // Podés agregar lógica en constructor, si querés un estado o fecha por defecto
    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "PENDIENTE";
        }
    }
}

