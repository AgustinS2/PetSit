package ar.edu.davinci.PetSit.controller.web.Postulacion;

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

import ar.edu.davinci.PetSit.domain.Postulacion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Postulacion.PostulacionService;

@Controller
@RequestMapping("/postulaciones")
public class PostulacionController {

    private final Logger LOGGER = LoggerFactory.getLogger(PostulacionController.class);

    @Autowired
    private PostulacionService postulacionService;

    @GetMapping("/list")
    public String listPostulaciones(Model model) {
        LOGGER.info("GET - listPostulaciones - /postulaciones/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Postulacion> postulaciones = postulacionService.list(pageable);
        model.addAttribute("listPostulaciones", postulaciones.getContent());
        model.addAttribute("pageNumber", postulaciones.getPageable().getPageNumber());
        model.addAttribute("totalPages", postulaciones.getTotalPages());
        return "postulaciones/list_postulaciones";
    }

    @GetMapping("/new")
    public String newPostulacionForm(Model model) {
        LOGGER.info("GET - newPostulacionForm - /postulaciones/new");
        Postulacion postulacion = new Postulacion();
        model.addAttribute("postulacion", postulacion);
        return "postulaciones/new_postulacion";
    }

    @PostMapping("/save")
    public String savePostulacion(@ModelAttribute("postulacion") Postulacion postulacion) {
        LOGGER.info("POST - savePostulacion - /postulaciones/save");
        try {
            if (postulacion.getId() == null) {
                postulacionService.save(postulacion);
            } else {
                postulacionService.update(postulacion);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "redirect:/postulaciones/list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editPostulacionForm(@PathVariable("id") Long id) {
        LOGGER.info("GET - editPostulacionForm - /postulaciones/edit/{id}");
        ModelAndView mav = new ModelAndView("postulaciones/edit_postulacion");
        try {
            Postulacion postulacion = postulacionService.findById(id);
            mav.addObject("postulacion", postulacion);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deletePostulacion(@PathVariable("id") Long id) {
        LOGGER.info("GET - deletePostulacion - /postulaciones/delete/{id}");
        postulacionService.delete(id);
        return "redirect:/postulaciones/list";
    }
}

