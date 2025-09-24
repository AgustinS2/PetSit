package ar.edu.davinci.PetSit.controller.web.Reporte;

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

import ar.edu.davinci.PetSit.domain.Reporte;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Reporte.ReporteService;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReporteController.class);

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/list")
    public String listReportes(Model model) {
        LOGGER.info("GET - listReportes - /reportes/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Reporte> reportes = reporteService.list(pageable);
        model.addAttribute("listReportes", reportes.getContent());
        model.addAttribute("pageNumber", reportes.getPageable().getPageNumber());
        model.addAttribute("totalPages", reportes.getTotalPages());
        return "reportes/list_reportes";
    }

    @GetMapping("/new")
    public String newReporteForm(Model model) {
        LOGGER.info("GET - newReporteForm - /reportes/new");
        Reporte reporte = new Reporte();
        model.addAttribute("reporte", reporte);
        return "reportes/new_reporte";
    }

    @PostMapping("/save")
    public String saveReporte(@ModelAttribute("reporte") Reporte reporte) {
        LOGGER.info("POST - saveReporte - /reportes/save");
        try {
            if (reporte.getId() == null) {
                reporteService.save(reporte);
            } else {
                reporteService.update(reporte);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "redirect:/reportes/list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editReporteForm(@PathVariable("id") Long id) {
        LOGGER.info("GET - editReporteForm - /reportes/edit/{id}");
        ModelAndView mav = new ModelAndView("reportes/edit_reporte");
        try {
            Reporte reporte = reporteService.findById(id);
            mav.addObject("reporte", reporte);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deleteReporte(@PathVariable("id") Long id) {
        LOGGER.info("GET - deleteReporte - /reportes/delete/{id}");
        reporteService.delete(id);
        return "redirect:/reportes/list";
    }
}

