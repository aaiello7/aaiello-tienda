package com.tiendaTech.tienda.controller;

import com.tiendaTech.tienda.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    // El servicio es final para asegurar la inmutabilidad
    private final CategoriaService categoriaService;

    // Inyección por constructor (No requiere @Autowired en Spring moderno)
    public CategoriaController(com.tiendaTech.tienda.service.CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String inicio(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());
        return "/categoria/listado";
    }
}