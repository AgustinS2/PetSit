package ar.edu.davinci.PetSit.controller.web.Refugio;

import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.domain.Veterinaria;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;

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
@RequestMapping("/petsit/refugios")
public class RefugioController {

    private final Logger LOGGER = LoggerFactory.getLogger(RefugioController.class);

    @Autowired private RefugioService refugioService;

    /**
     * Vista pública — muestra solo los refugios APROBADOS y activos.
     * Tanto /index como /list retornan el mismo template.
     */
    @GetMapping({"/index", "/list"})
    public String listRefugios(Model model) {
        LOGGER.info("GET /petsit/refugios/list");
        Pageable pageable = PageRequest.of(0, 50);
        Page<Refugio> refugios = refugioService.list(pageable);
        model.addAttribute("listVeterinarias", refugios.getContent());
        model.addAttribute("pageNumber", refugios.getPageable().getPageNumber());
        model.addAttribute("totalPages", refugios.getTotalPages());
        model.addAttribute("listRefugios", refugioService.listAprobadas());
        return "refugios/list_refugios";
    }

    @GetMapping("/new")
    public String newRefugioForm(Model model) {
        model.addAttribute("refugio", new Refugio());
        return "refugios/new_refugio";
    }

    @PostMapping("/save")
    public String saveRefugio(@ModelAttribute("refugio") Refugio refugio) {
        try {
            if (refugio.getId() == null) refugioService.save(refugio);
            else refugioService.update(refugio);
        } catch (BusinessException e) {
            LOGGER.error("Error guardando refugio: {}", e.getMessage());
        }
        return "redirect:/petsit/refugios/list";
    }

    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editRefugioForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("refugios/edit_refugio");
        try { mav.addObject("refugio", refugioService.findById(id)); }
        catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return mav;
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteRefugio(@PathVariable Long id) {
        refugioService.delete(id);
        return "redirect:/petsit/refugios/list";
    }
}
