package com.tiendaTech.tienda.controller;

import com.tiendaTech.tienda.domain.Constante;
import com.tiendaTech.tienda.service.ConstanteService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/constante")
public class ConstanteController {

    private final ConstanteService constanteService;
    private final MessageSource messageSource;

    public ConstanteController(ConstanteService constanteService, MessageSource messageSource) {
        this.constanteService = constanteService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var lista = constanteService.getConstantes();
        model.addAttribute("constantes", lista);
        model.addAttribute("totalConstantes", lista.size());
        return "/constante/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Constante constante, RedirectAttributes redirectAttributes) {
        constanteService.save(constante);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/constante/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idConstante, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            constanteService.delete(idConstante);
        } catch (IllegalArgumentException e) {
            titulo = "error"; // Captura la excepción de argumento inválido para el mensaje de "no existe"
            detalle = "constante.error01";
        } catch (IllegalStateException e) {
            titulo = "error"; // Captura la excepción de estado ilegal para el mensaje de "datos asociados"
            detalle = "constante.error02";
        } catch (Exception e) {
            titulo = "error";  // Captura cualquier otra excepción inesperada
            detalle = "constante.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/constante/listado";
    }

    @GetMapping("/modificar/{idConstante}")
    public String modificar(@PathVariable("idConstante") Integer idConstante, Model model, RedirectAttributes redirectAttributes) {
        try {
            Constante constante = constanteService.getConstante(idConstante);
            model.addAttribute("constante", constante);
            return "/constante/modifica";
        } catch (NoSuchElementException e) {
            // Captura la excepción de 'no encontrado' del servicio
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("constante.error01", null, Locale.getDefault()));
            return "redirect:/constante/listado";
        }
    }
}
