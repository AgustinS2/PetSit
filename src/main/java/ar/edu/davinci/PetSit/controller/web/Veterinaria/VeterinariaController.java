package ar.edu.davinci.PetSit.controller.web.Veterinaria;

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

import ar.edu.davinci.PetSit.domain.Veterinaria;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;

@Controller
@RequestMapping("/petsit/veterinarias")
public class VeterinariaController {

    private final Logger LOGGER = LoggerFactory.getLogger(VeterinariaController.class);

    @Autowired
    private VeterinariaService veterinariaService;

    @GetMapping("/index")
	    public String indexVeterinaria() {
	        return "veterinarias/list_veterinarias";
	}


    @GetMapping("/list")
    public String listVeterinarias(Model model) {
        LOGGER.info("GET - listVeterinarias - /veterinarias/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Veterinaria> veterinarias = veterinariaService.list(pageable);
        model.addAttribute("listVeterinarias", veterinarias.getContent());
        model.addAttribute("pageNumber", veterinarias.getPageable().getPageNumber());
        model.addAttribute("totalPages", veterinarias.getTotalPages());
        return "veterinarias/list_veterinarias";
    }

    @GetMapping("/new")
    public String newVeterinariaForm(Model model) {
        LOGGER.info("GET - newVeterinariaForm - /veterinarias/new");
        Veterinaria veterinaria = new Veterinaria();
        model.addAttribute("veterinaria", veterinaria);
        return "veterinarias/new_veterinaria";
    }

    @PostMapping("/save")
    public String saveVeterinaria(@ModelAttribute("veterinaria") Veterinaria veterinaria) {
        LOGGER.info("POST - saveVeterinaria - /veterinarias/save");
        try {
            if (veterinaria.getId() == null) {
                veterinariaService.save(veterinaria);
            } else {
                veterinariaService.update(veterinaria);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "redirect:/veterinarias/list";
    }

    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editVeterinariaForm(@PathVariable("id") Long id) {
        LOGGER.info("GET - editVeterinariaForm - /veterinarias/edit/{id}");
        ModelAndView mav = new ModelAndView("veterinarias/edit_veterinaria");
        try {
            Veterinaria veterinaria = veterinariaService.findById(id);
            mav.addObject("veterinaria", veterinaria);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteVeterinaria(@PathVariable("id") Long id) {
        LOGGER.info("GET - deleteVeterinaria - /veterinarias/delete/{id}");
        veterinariaService.delete(id);
        return "redirect:/veterinarias/list";
    }
}

