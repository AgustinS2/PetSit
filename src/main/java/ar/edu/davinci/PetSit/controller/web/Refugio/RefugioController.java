package ar.edu.davinci.PetSit.controller.web.Refugio;

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

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;

@Controller
@RequestMapping("/petsit/refugios")
public class RefugioController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(RefugioController.class);

    @Autowired
    private RefugioService refugioService;

    @GetMapping("/list")
    public String listRefugios(Model model) {
        LOGGER.info("GET - listRefugios - /refugios/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Refugio> refugios = refugioService.list(pageable);
        model.addAttribute("listRefugios", refugios.getContent());
        model.addAttribute("pageNumber", refugios.getPageable().getPageNumber());
        model.addAttribute("totalPages", refugios.getTotalPages());
        return "refugios/list_refugios";
    }

    @GetMapping("/new")
    public String newRefugioForm(Model model) {
        LOGGER.info("GET - newRefugioForm - /refugios/new");
        Refugio refugio = new Refugio();
        model.addAttribute("refugio", refugio);
        return "refugios/new_refugio";
    }

    @PostMapping("/save")
    public String saveRefugio(@ModelAttribute("refugio") Refugio refugio) {
        LOGGER.info("POST - saveRefugio - /refugios/save");
        try {
            if (refugio.getId() == null) {
                refugioService.save(refugio);
            } else {
                refugioService.update(refugio);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "redirect:/refugios/list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editRefugioForm(@PathVariable("id") Long id) {
        LOGGER.info("GET - editRefugioForm - /refugios/edit/{id}");
        ModelAndView mav = new ModelAndView("refugios/edit_refugio");
        try {
            Refugio refugio = refugioService.findById(id);
            mav.addObject("refugio", refugio);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deleteRefugio(@PathVariable("id") Long id) {
        LOGGER.info("GET - deleteRefugio - /refugios/delete/{id}");
        refugioService.delete(id);
        return "redirect:/refugios/list";
    }
}

