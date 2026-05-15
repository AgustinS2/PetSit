package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "adopciones")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Adopcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ado_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ado_mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "ado_usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "ado_fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "ado_estado", nullable = false)
    private String estado;

    @Column(name = "ado_descripcion")
    private String descripcion;

    @Column(name = "ado_foto")
    private String foto;

    /**
     * Ubicación donde se puede ver / retirar la mascota.
     * Puede ser una dirección libre o el nombre de un refugio.
     */
    @Column(name = "ado_ubicacion")
    private String ubicacion;

    /**
     * Si la adopción está vinculada a un refugio específico.
     * Opcional — puede ser null si es de un dueño particular.
     */
    @ManyToOne
    @JoinColumn(name = "ado_refugio_id", nullable = true)
    private Refugio refugio;

    @PrePersist
    public void prePersist() {
        if (fechaPublicacion == null) fechaPublicacion = LocalDateTime.now();
        if (estado == null) estado = "ACTIVA";
    }
}
