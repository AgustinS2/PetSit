package PetsitApp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import java.util.List;

@Entity
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String raza;
    private Integer edad;
    private String historialMedico;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario propietario;

    @OneToMany(mappedBy = "mascota")
    private List<Alerta> alertas;

    public void registrar() {  }
    public void actualizarDatos() {  }
    public void eliminar() {  }
}
