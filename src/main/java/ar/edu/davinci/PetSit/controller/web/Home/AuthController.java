package ar.edu.davinci.PetSit.controller.web.Home;

import ar.edu.davinci.PetSit.dto.RegisterForm;
import ar.edu.davinci.PetSit.service.Registro.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/petsit/home/register")
    public String showRegister(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "home/register"; // <- carpeta templates/home/register.html
    }

    @PostMapping("/petsit/home/register")
    public String doRegister(@ModelAttribute("form") RegisterForm form, Model model) {
        try {
            authService.registerUsuario(form);
            model.addAttribute("ok", "Usuario creado correctamente. Ya podés iniciar sesión.");
            model.addAttribute("form", new RegisterForm()); // limpia el form
            return "home/register";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "home/register";
        }
    }
}
