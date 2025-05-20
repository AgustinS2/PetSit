package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refugios")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
}

