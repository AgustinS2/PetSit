package ar.edu.davinci.PetSit.controller.web.Mascota;

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

@Controller
@RequestMapping("/petsit/mascotas")
public class MascotaController {

    private final Logger LOGGER = LoggerFactory.getLogger(MascotaController.class);

    @Autowired
    private MascotaService mascotaService;

    @GetMapping("/mis-mascotas")
	    public String misMascotas() {
	        return "mascotas/mis_mascotas"; // Renderiza templates/"""".html
	}
    
    @GetMapping("/adoptar")
	    public String adoptar() {
	        return "mascotas/adoptar_mascotas"; // Renderiza templates/"""".html
	}

    @GetMapping("/document")
	    public String document() {
	        return "mascotas/document_mascotas"; // Renderiza templates/"""".html
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

    @GetMapping("/edit/{id}")
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

    @GetMapping("/delete/{id}")
    public String deleteMascota(@PathVariable("id") Long id) {
        LOGGER.info("GET - deleteMascota - /mascotas/delete/{id}");
        mascotaService.delete(id);
        return "redirect:/mascotas/list";
    }
}

