package com.tiendaTech.tienda.controller;

import com.tiendaTech.tienda.domain.Ruta;
import com.tiendaTech.tienda.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rutas")
public class RutaController {

    @Autowired
    private RutaRepository rutaRepository;

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("rutas", rutaRepository.findAllByOrderByRequiereRolAsc());
        model.addAttribute("totalRutas", rutaRepository.count());
        model.addAttribute("ruta", new Ruta());
        return "/rutas/listado";
    }

    @PostMapping("/guardar")
    public String guardar(Ruta ruta) {
        rutaRepository.save(ruta);
        return "redirect:/rutas/listado";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Integer id, Model model) {
        Ruta ruta = rutaRepository.findById(id).orElse(null);
        model.addAttribute("ruta", ruta);
        return "/rutas/modifica";
    }

    @PostMapping("/eliminar")
    public String eliminar(Ruta ruta) {
        rutaRepository.delete(ruta);
        return "redirect:/rutas/listado";
    }
}