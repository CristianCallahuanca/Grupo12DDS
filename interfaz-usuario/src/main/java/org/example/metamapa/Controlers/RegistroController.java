package org.example.metamapa.Controlers;

import org.example.metamapa.models.dto.UsuarioRegistroDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    // 1. Mostrar formulario de registro
    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        // Crear un objeto VACÍO para el formulario
        model.addAttribute("usuarioForm", new UsuarioRegistroDTO());
        return "register";
    }

    // 2. Procesar el formulario cuando se envía
    @PostMapping("/register")
    public String procesarRegistro(@ModelAttribute("usuarioForm") UsuarioRegistroDTO usuarioForm) {
        System.out.println("Usuario a registrar: " + usuarioForm.getNombre());
        // Aquí guardarías en la base de datos
        return "redirect:/login?registroExitoso";
    }
}