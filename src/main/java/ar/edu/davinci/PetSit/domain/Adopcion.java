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
@Table(name = "adopciones")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Adopcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ado_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ado_mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ado_usuario_id", nullable = false)
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "ado_fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "ado_estado", nullable = false)
    private String estado; // Ejemplo: ACTIVA, FINALIZADA, CANCELADA, etc.

    @Column(name = "ado_descripcion")
    private String descripcion;
}
