package ar.edu.davinci.PetSit.controller.web.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.domain.TipoUsuario;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/petsit/usuarios")
public class UsuarioController extends PetSitApp {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private UsuarioService usuarioService;
	
    @GetMapping("/chat")
	    public String chat() {
	        return "usuarios/chat_usuarios"; // Renderiza templates/"""".html
	}

    @GetMapping("/perfil")
	    public String perfilUsuario(Model model, Principal principal) {
			LOGGER.info("GET - perfilUsuario - /petsit/usuarios/perfil");

			if (principal == null) {
				LOGGER.warn("No hay usuario logueado");
				return "redirect:/petsit/home/login";
			}

			try {
				// Buscar el usuario logueado por su correo (username)
				Usuario usuario = usuarioService.findByCorreo(principal.getName());
				List<Mascota> mascotas = usuario.getMascotas() != null ? usuario.getMascotas() : Collections.emptyList();

				model.addAttribute("usuario",usuario);
				model.addAttribute("mascotas", mascotas);
				model.addAttribute("cantidadMascotas", mascotas.size());
			

				LOGGER.info("Usuario cargado: {}", usuario.getCorreo());
			} catch (Exception e) {
				LOGGER.error("Error al cargar el perfil del usuario", e);
				return "redirect:/petsit/home/login";
			}

	        return "usuarios/perfil_usuarios"; // Renderiza templates/"""".html
	}

    @GetMapping("/index")
    public String indexUsuario(Model model, Principal principal) {
        LOGGER.info("GET - indexUsuario - /petsit/usuarios/index");

        if (principal == null) {
            return "redirect:/petsit/home/login";
        }

        try {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            model.addAttribute("usuario", usuario);
        } catch (Exception e) {
            LOGGER.error("Error al cargar usuario logueado", e);
            return "redirect:/petsit/home/login";
        }

        return "usuarios/user_index";
    }

	@GetMapping(path = "/usuarios/list")
	public String showUsuarioPage(Model model) {
		LOGGER.info("GET - showUsuarioPage - /usuarios/list");
		
		Pageable pageable = PageRequest.of(0, 20);
		Page<Usuario> usuarios = usuarioService.list(pageable);
		model.addAttribute("listUsuarios", usuarios.getContent());
		model.addAttribute("pageNumber", usuarios.getPageable().getPageNumber());
		model.addAttribute("totalPages", usuarios.getTotalPages());
		
		LOGGER.info("usuarios.size: " + usuarios.getNumberOfElements());
		return "usuarios/list_usuarios";
	}
	
	@GetMapping(path = "/usuarios/new")
	public String showNewUsuarioPage(Model model) {
	LOGGER.info("GET - showNewUsuarioPage - /usuarios/new");
	Usuario usuario = new Usuario();
	model.addAttribute("usuario", usuario);
	model.addAttribute("tipoUsuarios", usuarioService.getTipoUsuarios());
	LOGGER.info("usuarios: " + usuario.toString());
	return "usuarios/new_usuarios";
	}
	
	@PostMapping(value = "/usuarios/save")
	public String saveUsuario(@ModelAttribute("usuario") Usuario usuario) {
	    LOGGER.info("POST - saveUsuario - /usuarios/save");

	    try {
	        if (usuario.getId() == null) {
	            usuario.setTipo(TipoUsuario.DUENO);
	            usuarioService.save(usuario);
	        } else {
	            usuarioService.update(usuario);
	        }
	    } catch (BusinessException e) {
	        e.printStackTrace();
	    }
	    return "redirect:/petsit/home/login";
	}
	
	@RequestMapping(value = "/usuarios/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditUsuarioPage(@PathVariable(name = "id") Long usuarioId) {
	LOGGER.info("GET - showEditUsuarioPage - /usuarios/edit/{id}");
	LOGGER.info("usuario: " + usuarioId);
	ModelAndView mav = new ModelAndView("usuarios/edit_usuarios");
	Usuario usuario = null;
	try {
	usuario = usuarioService.findById(usuarioId);
	mav.addObject("usuario", usuario);
	mav.addObject("tipoUsuarioActual", usuario.getTipo());
	} catch (BusinessException e) {
	LOGGER.error("ERROR AL TRAER LA USUARIO");
	e.printStackTrace();
	}
	mav.addObject("tipoUsuarios", usuarioService.getTipoUsuarios());
	return mav;
	}
	
	@RequestMapping(value = "/usuarios/delete/{id}", method = RequestMethod.GET)
	public String deleteUsuario(@PathVariable(name = "id") Long usuarioId) {
	LOGGER.info("GET - deleteUsuario - /usuarios/delete/{id}");
	LOGGER.info("usuario: " + usuarioId);
	usuarioService.delete(usuarioId);
	return "redirect:/petsit/usuarios/list";
	}
}