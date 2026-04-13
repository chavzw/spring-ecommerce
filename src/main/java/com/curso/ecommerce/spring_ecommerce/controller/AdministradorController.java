package com.curso.ecommerce.spring_ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    
    @GetMapping("")
    public String home(){
        return "administrador/home";
    }
}
