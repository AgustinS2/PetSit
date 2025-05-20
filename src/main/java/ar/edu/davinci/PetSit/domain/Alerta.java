package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alertas")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Alerta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "alt_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alt_mascota_id")
    private Mascota mascota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "alt_usuario_id", nullable = false)
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "alt_fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(name = "alt_descripcion")
    private String descripcion;

    @Column(name = "alt_ubicacion")
    private String ubicacion;

    @Column(name = "alt_foto")
    private String foto;
}
