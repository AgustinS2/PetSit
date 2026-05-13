package ar.edu.davinci.PetSit.controller.web.Alerta;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.Alerta;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Alerta.AlertaService;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
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

import java.security.Principal;

@Controller
@RequestMapping("/petsit/alertas")
public class AlertaController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(AlertaController.class);

    @Autowired private AlertaService  alertaService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private MascotaService mascotaService;

    @GetMapping("/list")
    public String listAlertas(Model model) {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Alerta> alertas = alertaService.list(pageable);
        model.addAttribute("listAlertas", alertas.getContent());
        model.addAttribute("pageNumber",  alertas.getPageable().getPageNumber());
        model.addAttribute("totalPages",  alertas.getTotalPages());
        return "alertas/list_alertas";
    }

    @GetMapping("/new")
    public String newAlertaForm(Model model, Principal principal) throws BusinessException {
        model.addAttribute("alerta", new Alerta());
        model.addAttribute("listMascotas",
                principal != null
                        ? mascotaService.findByDueno(usuarioService.findByCorreo(principal.getName()))
                        : mascotaService.list());
        return "alertas/new_alertas";
    }

    @PostMapping("/save")
    public String saveAlerta(@ModelAttribute("alerta") Alerta alerta, Principal principal) {
        try {
            if (principal != null && alerta.getUsuario() == null) {
                Usuario usuario = usuarioService.findByCorreo(principal.getName());
                alerta.setUsuario(usuario);
            }
            if (alerta.getId() == null) alertaService.save(alerta);
            else alertaService.update(alerta);
        } catch (BusinessException e) {
            LOGGER.error("Error guardando alerta: {}", e.getMessage());
        }
        return "redirect:/petsit/alertas/list";
    }

    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editAlertaForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("alertas/edit_alertas");
        try { mav.addObject("alerta", alertaService.findById(id)); }
        catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return mav;
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteAlerta(@PathVariable Long id) {
        alertaService.delete(id);
        return "redirect:/petsit/alertas/list";
    }
}
