package org.example.metamapa.Controlers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.metamapa.models.dto.HechoDTO;
import org.example.metamapa.services.HechoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
     private final HechoService hechoService;


    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('CONTRIBUYENTE')")
    public String mostrarFormularioCrear(Model model) {
        // La lista de categorías predefinidas para el desplegable
        List<String> categorias = Arrays.asList("vientos fuertes",
                "inundaciones",
                "granizo",
                "nevadas",
                "calor extremo",
                "sequía",
                "derrumbes",
                "actividad volcánica",
                "incendios",
                "contaminación",
                "evento sanitario",
                "derrame",
                "intoxicación masiva");

        model.addAttribute("hechoForm", new HechoDTO());
        model.addAttribute("listaCategorias", categorias); // Mandamos la lista a la vista
        model.addAttribute("titulo", "Reportar Nuevo Hecho");

        return "hechos/crear";
    }

    @PostMapping("/crear")
    public String crearHecho(@Valid @ModelAttribute("hechoForm") HechoDTO hechoForm,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Authentication authentication,
                             Model model) {

        // Lógica para manejar la categoría "Otra"
        if ("OTRA".equals(hechoForm.getCategoria()) && (hechoForm.getCategoriaOtra() == null || hechoForm.getCategoriaOtra().isBlank())) {
            // Si el usuario eligió "Otra" pero no escribió nada, agregamos un error manual.
            bindingResult.rejectValue("categoriaOtra", "error.hechoForm", "Si eliges 'Otra', debes especificar la categoría.");
        }

        if (bindingResult.hasErrors()) {
            // Si hay errores, volvemos a mostrar el formulario sin perder los datos
            List<String> categorias = Arrays.asList("Robo", "Accidente de Tránsito", "Incendio", "Acto Vandálico");
            model.addAttribute("listaCategorias", categorias); // Volvemos a mandar la lista
            model.addAttribute("titulo", "Reportar Nuevo Hecho");
            return "hechos/crear";
        }

        try {
            // Asignamos el ID del usuario logueado
            String contribuyenteID = authentication.getName(); // Obtiene el username
            hechoForm.setContribuyenteID(contribuyenteID);

            // Si se eligió "Otra", usamos el valor de categoriaOtra
            if ("OTRA".equals(hechoForm.getCategoria())) {
                hechoForm.setCategoria(hechoForm.getCategoriaOtra());
            }

            // Aquí llamarías a tu servicio para guardar el hecho
             HechoDTO hechoCreado = hechoService.crearHecho(hechoForm);

            // Simulación de un hecho creado para la redirección
            long idHechoCreado = 123L;

            redirectAttributes.addFlashAttribute("mensaje", "Hecho reportado exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

            // Redirigimos a la página de detalle del hecho recién creado
            return "redirect:/hechos/" + idHechoCreado;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al reportar el hecho: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/hechos/nuevo";
        }
    }

}
