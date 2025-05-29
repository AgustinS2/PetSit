package ar.edu.davinci.PetSit.controller.web.Adopcion;

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

import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;

@Controller
@RequestMapping("/adopciones")
public class AdopcionController {

    private final Logger LOGGER = LoggerFactory.getLogger(AdopcionController.class);

    @Autowired
    private AdopcionService adopcionService;

    @GetMapping("/list")
    public String listAdopciones(Model model) {
        LOGGER.info("GET - listAdopciones - /adopciones/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Adopcion> adopciones = adopcionService.list(pageable);
        model.addAttribute("listAdopciones", adopciones.getContent());
        model.addAttribute("pageNumber", adopciones.getPageable().getPageNumber());
        model.addAttribute("totalPages", adopciones.getTotalPages());
        return "adopciones/list_adopciones";
    }

    @GetMapping("/new")
    public String newAdopcionForm(Model model) {
        LOGGER.info("GET - newAdopcionForm - /adopciones/new");
        Adopcion adopcion = new Adopcion();
        model.addAttribute("adopcion", adopcion);
        return "adopciones/new_adopcion";
    }

    @PostMapping("/save")
    public String saveAdopcion(@ModelAttribute("adopcion") Adopcion adopcion) {
        LOGGER.info("POST - saveAdopcion - /adopciones/save");
        try {
            if (adopcion.getId() == null) {
                adopcionService.save(adopcion);
            } else {
                adopcionService.update(adopcion);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "redirect:/adopciones/list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editAdopcionForm(@PathVariable("id") Long id) {
        LOGGER.info("GET - editAdopcionForm - /adopciones/edit/{id}");
        ModelAndView mav = new ModelAndView("adopciones/edit_adopcion");
        try {
            Adopcion adopcion = adopcionService.findById(id);
            mav.addObject("adopcion", adopcion);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deleteAdopcion(@PathVariable("id") Long id) {
        LOGGER.info("GET - deleteAdopcion - /adopciones/delete/{id}");
        adopcionService.delete(id);
        return "redirect:/adopciones/list";
    }
}

