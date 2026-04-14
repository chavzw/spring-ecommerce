package com.curso.ecommerce.spring_ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.spring_ecommerce.model.Producto;
import com.curso.ecommerce.spring_ecommerce.model.Usuario;
import com.curso.ecommerce.spring_ecommerce.service.ProductoService;
import com.curso.ecommerce.spring_ecommerce.service.UploadFileService;

import org.springframework.ui.Model;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(){
        return "productos/create";
    }


    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, 
                    RedirectAttributes attributes) throws IOException {
        try {
            LOGGER.info("Este es el producto {}", producto);
            Usuario u = new Usuario(1, "", "", "", "", "", "", "");
            producto.setUsuario(u);

            if (producto.getId() == null) {
                String nombreImagen = upload.saveImage(file);
                producto.setImagen(nombreImagen);
            }

            productoService.save(producto);
            attributes.addFlashAttribute("msg", "Producto guardado correctamente");
            attributes.addFlashAttribute("msgClass", "alert-success");

        } catch (Exception e) {
            attributes.addFlashAttribute("msg", "Error al guardar el producto");
            attributes.addFlashAttribute("msgClass", "alert-danger");
        }

        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();

        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto);

        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        Producto p = productoService.get(producto.getId()).get();

        producto.setUsuario(p.getUsuario());

        if (file.isEmpty()) {
            producto.setImagen(p.getImagen());
        } else {
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        productoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
    Producto p = productoService.get(id).get();
    
        // ✅ Elimina solo si NO es default.jpg
        if (!p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }
    
        productoService.delete(id);
        return "redirect:/productos";
    }
}