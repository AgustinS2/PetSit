package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veterinarias")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Veterinaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "vet_id")
    private Long id;

    @Column(name = "vet_nombre")
    private String nombre;

    @Column(name = "vet_direccion")
    private String direccion;

    @Column(name = "vet_telefono")
    private String telefono;

    @Column(name = "vet_horario_atencion")
    private String horarioAtencion;

    @Column(name = "vet_ubicacion")
    private String ubicacion;

    @Column(name = "vet_activa")
    private Boolean activa;

    @Column(name = "vet_foto")
    private String foto;

    @Column(name = "vet_lat")
    private Double lat;

    @Column(name = "vet_lng")
    private Double lng;

    /**
     * Estado de aprobación: PENDIENTE → admin la aprueba → pasa a activa=true.
     * Cuando alguien registra una vet desde el formulario público,
     * queda en estado PENDIENTE hasta que el admin la aprueba.
     */
    @Column(name = "vet_estado_aprobacion")
    private String estadoAprobacion; // PENDIENTE / APROBADA / RECHAZADA
}
