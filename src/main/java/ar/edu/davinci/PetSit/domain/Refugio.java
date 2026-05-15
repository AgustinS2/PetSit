package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refugios")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Refugio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ref_id")
    private Long id;

    @Column(name = "ref_nombre", nullable = false)
    private String nombre;

    @Column(name = "ref_direccion")
    private String direccion;

    @Column(name = "ref_telefono")
    private String telefono;

    @Column(name = "ref_correo")
    private String correo;

    @Column(name = "ref_foto")
    private String foto;

    @Column(name = "ref_ubicacion")
    private String ubicacion;

    @Column(name = "ref_lat")
    private Double lat;

    @Column(name = "ref_lng")
    private Double lng;

    /** Visible en listados y mapa cuando es true */
    @Column(name = "ref_activa")
    private Boolean activa;

    /**
     * PENDIENTE → espera aprobación del admin
     * APROBADA  → visible (activa=true)
     * RECHAZADA → no visible (activa=false)
     */
    @Column(name = "ref_estado_aprobacion")
    private String estadoAprobacion;
}
