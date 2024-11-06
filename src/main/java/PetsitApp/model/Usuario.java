package PetsitApp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String correo;
    private String contraseña;
    private String tipoUsuario;

    @OneToMany(mappedBy = "propietario")
    private List<Mascota> mascotas;

    @OneToMany(mappedBy = "usuario")
    private List<Alerta> alertas;

    public void registrar() { /* Lógica para registrar usuario */ }
    public void iniciarSesion() { /* Lógica para iniciar sesión */ }
}
