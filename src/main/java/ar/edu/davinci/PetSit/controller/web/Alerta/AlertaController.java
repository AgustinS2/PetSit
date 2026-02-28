package ar.edu.davinci.PetSit.controller.web.Alerta;

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

import ar.edu.davinci.PetSit.domain.Alerta;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Alerta.AlertaService;

@Controller
@RequestMapping("/alertas")
public class AlertaController {

    private final Logger LOGGER = LoggerFactory.getLogger(AlertaController.class);

    @Autowired
    private AlertaService alertaService;

    @GetMapping("/list")
    public String listAlertas(Model model) {
        LOGGER.info("GET - listAlertas - /alertas/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Alerta> alertas = alertaService.list(pageable);
        model.addAttribute("listAlertas", alertas.getContent());
        model.addAttribute("pageNumber", alertas.getPageable().getPageNumber());
        model.addAttribute("totalPages", alertas.getTotalPages());
        return "alertas/list_alertas";
    }

    @GetMapping("/new")
    public String newAlertaForm(Model model) {
        LOGGER.info("GET - newAlertaForm - /alertas/new");
        Alerta alerta = new Alerta();
        model.addAttribute("alerta", alerta);
        return "alertas/new_alerta";
    }

    @PostMapping("/save")
    public String saveAlerta(@ModelAttribute("alerta") Alerta alerta) {
        LOGGER.info("POST - saveAlerta - /alertas/save");
        try {
            if (alerta.getId() == null) {
                alertaService.save(alerta);
            } else {
                alertaService.update(alerta);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "redirect:/alertas/list";
    }

    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editAlertaForm(@PathVariable("id") Long id) {
        LOGGER.info("GET - editAlertaForm - /alertas/edit/{id}");
        ModelAndView mav = new ModelAndView("alertas/edit_alerta");
        try {
            Alerta alerta = alertaService.findById(id);
            mav.addObject("alerta", alerta);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteAlerta(@PathVariable("id") Long id) {
        LOGGER.info("GET - deleteAlerta - /alertas/delete/{id}");
        alertaService.delete(id);
        return "redirect:/alertas/list";
    }
}

