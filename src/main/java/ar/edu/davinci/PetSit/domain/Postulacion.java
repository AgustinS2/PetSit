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
@Table(name = "postulaciones")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Postulacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "pos_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pos_adopcion_id", nullable = false)
    private Adopcion adopcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pos_usuario_id", nullable = false)
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "pos_fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(name = "pos_mensaje")
    private String mensaje;
}
