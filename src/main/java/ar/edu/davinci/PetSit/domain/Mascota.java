package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mascotas")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Mascota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msc_id")
    private Long id;

    @Column(name = "msc_nombre")
    private String nombre;

    @Column(name = "msc_raza")
    private String raza;

    @Column(name = "msc_color")
    private String color;

    @Column(name = "msc_tamano")
    private String tamano; // chico, mediano, grande

    @Column(name = "msc_edad")
    private Integer edad;

    @Column(name = "msc_descripcion")
    private String descripcion;

    @Column(name = "msc_foto")
    private String foto;

    @ManyToOne
    @JoinColumn(name = "msc_dueno_id")
    private Usuario dueno;

    @Column(name = "msc_sexo")
    private String sexo;

    @Column(name = "msc_fecha_nacimiento")
    private LocalDate fechaNacimiento;

}

