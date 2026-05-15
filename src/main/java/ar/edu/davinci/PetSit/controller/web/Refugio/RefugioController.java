package ar.edu.davinci.PetSit.controller.web.Refugio;

import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        // Solo los aprobados y activos para la vista pública
        List<Refugio> aprobados = refugioService.listAprobadas()
                .stream()
                .filter(r -> Boolean.TRUE.equals(r.getActiva()))
                .toList();
        model.addAttribute("listRefugios", aprobados);
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
