package com.curso.ecommerce.spring_ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.spring_ecommerce.model.Producto;
import com.curso.ecommerce.spring_ecommerce.service.ProductoService;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    
    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String home(Model model){
        
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "administrador/home";
    }
}
