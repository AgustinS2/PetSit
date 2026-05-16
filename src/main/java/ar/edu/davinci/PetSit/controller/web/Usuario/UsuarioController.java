package ar.edu.davinci.PetSit.controller.web.Usuario;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.*;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import ar.edu.davinci.PetSit.service.Postulacion.PostulacionService;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/petsit/usuarios")
public class UsuarioController extends PetSitApp {

	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired private UsuarioService     usuarioService;
	@Autowired private MascotaService     mascotaService;
	@Autowired private PostulacionService postulacionService;
	@Autowired private RefugioService     refugioService;
	@Autowired private VeterinariaService veterinariaService;
	@Autowired private AdopcionService    adopcionService;

	// ── INDEX ─────────────────────────────────────────────────────────────────
	@GetMapping("/index")
	public String indexUsuario(Model model, Principal principal) {
		if (principal == null) return "redirect:/petsit/home/login";
		try {
			Usuario usuario = usuarioService.findByCorreo(principal.getName());
			model.addAttribute("usuario", usuario);
			// Si es admin, redirigir al panel admin
			if (usuario.getTipo() == TipoUsuario.ADMINISTRADOR) {
				return "redirect:/petsit/admin/index";
			}
		} catch (Exception e) {
			return "redirect:/petsit/home/login";
		}
		return "usuarios/user_index";
	}

	// ── PERFIL ────────────────────────────────────────────────────────────────
	@GetMapping("/perfil")
	public String perfilUsuario(Model model, Principal principal) {
		if (principal == null) return "redirect:/petsit/home/login";
		try {
			Usuario usuario = usuarioService.findByCorreo(principal.getName());
			List<Mascota>     mascotas     = usuario.getMascotas() != null ? usuario.getMascotas() : Collections.emptyList();
			List<Postulacion> postulaciones = postulacionService.findByUsuario(usuario);
			model.addAttribute("usuario",           usuario);
			model.addAttribute("mascotas",           mascotas);
			model.addAttribute("cantidadMascotas",   mascotas.size());
			model.addAttribute("cantidadSolicitudes", postulaciones.size());
		} catch (Exception e) {
			return "redirect:/petsit/home/login";
		}
		return "usuarios/perfil_usuarios";
	}

	// ── MIS SOLICITUDES ───────────────────────────────────────────────────────
	@GetMapping("/mis-solicitudes")
	public String misSolicitudes(Model model, Principal principal) throws BusinessException {
		if (principal == null) return "redirect:/petsit/home/login";
		Usuario usuario = usuarioService.findByCorreo(principal.getName());
		List<Postulacion> postulaciones = postulacionService.findByUsuario(usuario);
		model.addAttribute("usuario",           usuario);
		model.addAttribute("listPostulaciones", postulaciones);
		return "usuarios/mis-solicitudes";
	}

	// ── VISTAS DE USUARIO ─────────────────────────────────────────────────────
	@GetMapping("/usuarioadoptar")
	public String usuarioAdoptar(Model model, Principal principal) throws BusinessException {
		if (principal == null) return "redirect:/petsit/home/login";
		Usuario usuario = usuarioService.findByCorreo(principal.getName());
		model.addAttribute("usuario",       usuario);
		model.addAttribute("listAdopciones", adopcionService.list());
		return "usuarios/usuarioadoptar";
	}

	@GetMapping("/usuariorefugio")
	public String usuarioRefugio(Model model, Principal principal) throws BusinessException {
		if (principal == null) return "redirect:/petsit/home/login";
		Usuario usuario = usuarioService.findByCorreo(principal.getName());
		model.addAttribute("usuario", usuario);
		// listAprobadas() incluye APROBADA y NULL (filas viejas sin el campo seteado)
		model.addAttribute("listRefugios", refugioService.listAprobadas());
		return "usuarios/usuariorefugio";
	}

	@GetMapping("/usuarioveterinarias")
	public String usuarioVeterinarias(Model model, Principal principal) throws BusinessException {
		if (principal == null) return "redirect:/petsit/home/login";
		Usuario usuario = usuarioService.findByCorreo(principal.getName());
		model.addAttribute("usuario",          usuario);
		model.addAttribute("listVeterinarias", veterinariaService.list());
		return "usuarios/usuarioveterinarias";
	}

	@GetMapping("/usuariocontacto")
	public String usuarioContacto(Model model, Principal principal) throws BusinessException {
		if (principal != null) model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
		return "usuarios/usuariocontacto";
	}

	@GetMapping("/usuarioquienessomos")
	public String usuarioQuienesSomos(Model model, Principal principal) throws BusinessException {
		if (principal != null) model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
		return "usuarios/usuarioquienessomos";
	}

	@GetMapping("/chat")
	public String chat(Model model, Principal principal) throws BusinessException {
		if (principal == null) return "redirect:/petsit/home/login";
		model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
		return "usuarios/chat_usuarios";
	}

	// ── REGISTRO (POST público) ───────────────────────────────────────────────
	/**
	 * Recibe el tipo desde el campo hidden del formulario de registro.
	 * El tipo ADMINISTRADOR solo se acepta si la clave admin es correcta —
	 * esa validación la hace el JS en el frontend; acá simplemente confiamos
	 * en el valor enviado porque el form ya lo validó.
	 * En producción: mover la validación de clave al servidor.
	 */
	@PostMapping("/save")
	public String saveUsuario(@ModelAttribute("usuario") Usuario usuario,
							  @RequestParam(value = "tipo", required = false) String tipoParam) {
		try {
			// El tipo viene como String desde el <input hidden>; lo parseamos
			if (tipoParam != null && !tipoParam.isBlank()) {
				try {
					usuario.setTipo(TipoUsuario.valueOf(tipoParam));
				} catch (IllegalArgumentException ex) {
					usuario.setTipo(TipoUsuario.DUENO); // fallback seguro
				}
			} else {
				usuario.setTipo(TipoUsuario.DUENO);
			}
			usuarioService.save(usuario);
		} catch (BusinessException e) {
			LOGGER.error("Error guardando usuario: {}", e.getMessage());
		}
		return "redirect:/petsit/home/login";
	}

	// ── CRUD ADMIN ────────────────────────────────────────────────────────────
	@GetMapping("/list")
	public String listUsuarios(Model model) {
		Pageable pageable = PageRequest.of(0, 20);
		Page<Usuario> usuarios = usuarioService.list(pageable);
		model.addAttribute("listUsuarios", usuarios.getContent());
		model.addAttribute("pageNumber",   usuarios.getPageable().getPageNumber());
		model.addAttribute("totalPages",   usuarios.getTotalPages());
		return "usuarios/list_usuarios";
	}

	@GetMapping("/new")
	public String newUsuarioForm(Model model) {
		model.addAttribute("usuario",      new Usuario());
		model.addAttribute("tipoUsuarios", usuarioService.getTipoUsuarios());
		return "usuarios/new_usuarios";
	}

	@GetMapping("/edit/{id:\\d+}")
	public ModelAndView editUsuarioForm(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView("usuarios/edit_usuarios");
		try {
			Usuario usuario = usuarioService.findById(id);
			mav.addObject("usuario",           usuario);
			mav.addObject("tipoUsuarios",      usuarioService.getTipoUsuarios());
			mav.addObject("tipoUsuarioActual", usuario.getTipo());
		} catch (BusinessException e) { LOGGER.error(e.getMessage()); }
		return mav;
	}

	@GetMapping("/delete/{id:\\d+}")
	public String deleteUsuario(@PathVariable Long id) {
		usuarioService.delete(id);
		return "redirect:/petsit/usuarios/list";
	}
}
