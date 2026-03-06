/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaTech.tienda.controller;

import com.tiendaTech.tienda.service.ProductoService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ProductoService productoService;

    public ConsultaController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        return "/consultas/listado";
    }

    @PostMapping("/consultaDerivada")
    public String consultaDerivada(@RequestParam() double precioInf,
            @RequestParam() double precioSup, Model model) {
        var lista = productoService.consultaDerivada(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaJPQL")
    public String consultaJPQL(@RequestParam() double precioInf,
            @RequestParam() double precioSup,
            Model model) {
        var productos = productoService.consultaJPQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaSQL")
    public String consultaSQL(@RequestParam() double precioInf,
            @RequestParam() double precioSup, Model model) {
        var lista = productoService.consultaSQL(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

}