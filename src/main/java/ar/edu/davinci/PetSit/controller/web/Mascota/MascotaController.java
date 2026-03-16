package ar.edu.davinci.PetSit.controller.web.Mascota;

import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
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

import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/petsit/mascotas")
public class MascotaController {

    private final Logger LOGGER = LoggerFactory.getLogger(MascotaController.class);

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private UsuarioService usuarioService;

    
    @GetMapping("/adoptar")
	    public String adoptar() {
	        return "mascotas/adoptar_mascotas";
	}

    @GetMapping("/document")
	    public String document() {
	        return "mascotas/document_mascotas";
	}

    @GetMapping("/list")
    public String listMascotas(Model model) {
        LOGGER.info("GET - listMascotas - /mascotas/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Mascota> mascotas = mascotaService.list(pageable);
        model.addAttribute("listMascotas", mascotas.getContent());
        model.addAttribute("pageNumber", mascotas.getPageable().getPageNumber());
        model.addAttribute("totalPages", mascotas.getTotalPages());
        return "mascotas/list_mascotas";
    }

    @GetMapping("/mis-mascotas")
    public String misMascotas(Model model, Principal principal) throws BusinessException {

        Usuario usuario = usuarioService.findByCorreo(principal.getName());

        List<Mascota> mascotas = mascotaService.findByDueno(usuario);

        model.addAttribute("listMascotas", mascotas);

        return "mascotas/list_mascotas";
    }

    @GetMapping("/new")
    public String newMascotaForm(Model model) {
        LOGGER.info("GET - newMascotaForm - /mascotas/new");
        Mascota mascota = new Mascota();
        model.addAttribute("mascota", mascota);
        return "mascotas/new_mascota";
    }

    @PostMapping("/save")
    public String saveMascota(@ModelAttribute("mascota") Mascota mascota) {
        LOGGER.info("POST - saveMascota - /mascotas/save");
        try {
            if (mascota.getId() == null) {
                mascotaService.save(mascota);
            } else {
                mascotaService.update(mascota);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "redirect:/mascotas/list";
    }

    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editMascotaForm(@PathVariable("id") Long id) {
        LOGGER.info("GET - editMascotaForm - /mascotas/edit/{id}");
        ModelAndView mav = new ModelAndView("mascotas/edit_mascota");
        try {
            Mascota mascota = mascotaService.findById(id);
            mav.addObject("mascota", mascota);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteMascota(@PathVariable("id") Long id) {
        LOGGER.info("GET - deleteMascota - /mascotas/delete/{id}");
        mascotaService.delete(id);
        return "redirect:/mascotas/list";
    }
}

