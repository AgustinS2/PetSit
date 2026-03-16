package ar.edu.davinci.PetSit.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// Configuración inical de JPA de una entidad
@Entity
@Table(name="usuarios")
// Configuración de Lombok
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Usuario implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = -8359168975855133954L;
	// Configurar por JPA cual el PK de la tabla usuarios
	@Id
	// Configurar la estragia de generación de los ids por JPA
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	// Configuramos por JPA el nombre de la columna
	@Column(name = "usr_id")
	private Long id;
	@Column(name = "usr_nombre", nullable = false)
	private String nombre;
	@Column(name = "usr_apellido", nullable = false)
	private String apellido;
	@Column(name = "usr_correo", nullable = false)
	private String correo;
	@Column(name = "usr_tipo_usuario")
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo;
	@Column(name = "usr_contrasena", nullable = false)
	private String contrasena;
	@Column(name = "usr_telefono", nullable = false)
	private String telefono;
	@Column (name = "usr_avatar", nullable = true)
	private String fotoPerfil;
	@OneToMany(mappedBy = "dueno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Mascota> mascotas = new ArrayList<>();

}
