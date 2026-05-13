package ar.edu.davinci.PetSit.controller.web.Veterinaria;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.Veterinaria;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
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

@Controller
@RequestMapping("/petsit/veterinarias")
public class VeterinariaController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(VeterinariaController.class);

    @Autowired
    private VeterinariaService veterinariaService;

    @GetMapping({"/index", "/list"})
    public String listVeterinarias(Model model) {
        LOGGER.info("GET /petsit/veterinarias/list");
        Pageable pageable = PageRequest.of(0, 50);
        Page<Veterinaria> veterinarias = veterinariaService.list(pageable);
        model.addAttribute("listVeterinarias", veterinarias.getContent());
        model.addAttribute("pageNumber", veterinarias.getPageable().getPageNumber());
        model.addAttribute("totalPages", veterinarias.getTotalPages());
        return "veterinarias/list_veterinarias";
    }

    @GetMapping("/new")
    public String newVeterinariaForm(Model model) {
        model.addAttribute("veterinaria", new Veterinaria());
        return "veterinarias/new_veterinaria";
    }

    @PostMapping("/save")
    public String saveVeterinaria(@ModelAttribute("veterinaria") Veterinaria veterinaria) {
        try {
            if (veterinaria.getId() == null) {
                veterinariaService.save(veterinaria);
            } else {
                veterinariaService.update(veterinaria);
            }
        } catch (BusinessException e) {
            LOGGER.error("Error guardando veterinaria: {}", e.getMessage());
        }
        return "redirect:/petsit/veterinarias/list";   // ← CORREGIDO
    }

    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editVeterinariaForm(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("veterinarias/edit_veterinaria");
        try {
            mav.addObject("veterinaria", veterinariaService.findById(id));
        } catch (BusinessException e) {
            LOGGER.error("Veterinaria no encontrada: {}", e.getMessage());
        }
        return mav;
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteVeterinaria(@PathVariable("id") Long id) {
        veterinariaService.delete(id);
        return "redirect:/petsit/veterinarias/list";   // ← CORREGIDO
    }
}
