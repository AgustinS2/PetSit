package ar.edu.davinci.PetSit.service.Registro;

import ar.edu.davinci.PetSit.domain.TipoUsuario;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.RegisterForm;
import ar.edu.davinci.PetSit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void registerUsuario(RegisterForm form) {

        String email = form.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(email)) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese correo.");
        }

        Usuario usuario = Usuario.builder()
                .nombre(form.getNombre())
                .apellido(form.getApellido())
                .correo(email)
                .telefono(form.getTelefono())
                .tipo(TipoUsuario.DUENO) // Solo el usuario comun
                .contrasena(passwordEncoder.encode(form.getContrasena()))
                .build();

        usuarioRepository.save(usuario);
    }
}
