package ar.edu.davinci.PetSit.security;

import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;
/*
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        try {
            // Buscamos el usuario por correo
            Usuario usuario = usuarioService.findByCorreo(correo);

            // Obtenemos el rol a partir del TipoUsuario (por ejemplo ADMIN, USER)
            String rol = usuario.getTipo().name();

            // Retornamos un objeto UserDetails compatible con Spring Security
            return User.builder()
                    .username(usuario.getCorreo())
                    .password(usuario.getContrasena()) // debe estar encriptada
                    .roles(rol)
                    .build();

        } catch (Exception e) {
            throw new UsernameNotFoundException("Error al buscar el usuario: " + correo, e);
        }
    }*/
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        try {
            System.out.println("Intentando login con correo: " + correo);

            Usuario usuario = usuarioService.findByCorreo(correo);

            System.out.println("Usuario encontrado: " + usuario.getCorreo());
            System.out.println("Hash guardado: " + usuario.getContrasena());
            System.out.println("Tipo: " + usuario.getTipo());

            return User.builder()
                    .username(usuario.getCorreo())
                    .password(usuario.getContrasena())
                    .roles(usuario.getTipo().name())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Error al buscar el usuario: " + correo, e);
        }
    }
}
