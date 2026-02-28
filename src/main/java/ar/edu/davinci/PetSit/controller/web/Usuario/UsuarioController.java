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
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;

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
	    public String perfilUsuario() {
	        return "usuarios/perfil_usuarios"; // Renderiza templates/"""".html
	}

	@GetMapping(path = "/list")
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
	
	@GetMapping(path = "/new")
	public String showNewUsuarioPage(Model model) {
	LOGGER.info("GET - showNewUsuarioPage - /usuarios/new");
	Usuario usuario = new Usuario();
	model.addAttribute("usuario", usuario);
	model.addAttribute("tipoUsuarios", usuarioService.getTipoUsuarios());
	LOGGER.info("usuarios: " + usuario.toString());
	return "usuarios/new_usuarios";
	}
	
	@GetMapping(path = "/index")
	public String indexUsuario(Model model) {
	LOGGER.info("GET - indexUsuario - /usuarios/index");
	return "usuarios/user_index";
	}
	
	@PostMapping(value = "/save")
	public String saveUsuario(@ModelAttribute("usuario") Usuario usuario) {
	LOGGER.info("POST - saveUsuario - /usuarios/save");
	
	
	LOGGER.info("usuario: " + usuario.toString());
	try {
	if (usuario.getId() == null) {
	usuarioService.save(usuario);
	} else {
	usuarioService.update(usuario);
	}
	} catch (BusinessException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	return "redirect:/petsit/usuarios/list";

	}
	
	@RequestMapping(value = "/edit/{id:\\d+}", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.GET)
	public String deleteUsuario(@PathVariable(name = "id") Long usuarioId) {
	LOGGER.info("GET - deleteUsuario - /usuarios/delete/{id}");
	LOGGER.info("usuario: " + usuarioId);
	usuarioService.delete(usuarioId);
	return "redirect:/petsit/usuarios/list";
	}

	@GetMapping("/usuarioadoptar")
	public String usuarioAdoptar() {
		return "usuarios/usuarioadoptar";
	}

	@GetMapping("/usuariocontacto")
	public String usuarioContacto() {
		return "usuarios/usuariocontacto";
	}

	@GetMapping("/usuariomapa")
	public String usuarioMapa() {
		return "usuarios/usuariomapa";
	}

	@GetMapping("/usuarioquienessomos")
	public String usuarioQuienesSomos() {
		return "usuarios/usuarioquienessomos";
	}

	@GetMapping("/usuariorefugio")
	public String usuarioRefugio() {
		return "usuarios/usuariorefugio";
	}

	@GetMapping("/usuarioveterinarias")
	public String usuarioVeterinarias() {
		return "usuarios/usuarioveterinarias";
	}

	@GetMapping("/mis-solicitudes")
	public String mis_solicitudes() {
		return "usuarios/mis-solicitudes";
	}

}