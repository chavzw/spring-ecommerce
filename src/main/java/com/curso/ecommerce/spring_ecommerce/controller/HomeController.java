package com.curso.ecommerce.spring_ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.spring_ecommerce.model.DetalleOrden;
import com.curso.ecommerce.spring_ecommerce.model.Orden;
import com.curso.ecommerce.spring_ecommerce.model.Producto;
import com.curso.ecommerce.spring_ecommerce.service.ProductoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;

    //almacenar los detalles de la orden
    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    //almacenar los datos de la orden
    Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model){
        log.info("Id producto enviado como parámentro {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();
        model.addAttribute("producto", producto);
        
        return "usuario/productoHome";
    }

    @PostMapping("/cart")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addCart(@RequestParam Integer id, @RequestParam Integer cantidad){
        Map<String, String> response = new HashMap<>();
        
        try {
            Optional<Producto> optionalProducto = productoService.get(id);
            Producto producto = optionalProducto.get();
            Integer idProducto = producto.getId();

            Optional<DetalleOrden> detalleExistente = detalles.stream()
                .filter(p -> p.getProducto().getId().equals(idProducto))
                .findFirst();

            if (detalleExistente.isPresent()) {
                DetalleOrden detalle = detalleExistente.get();
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                detalle.setTotal(detalle.getPrecio() * detalle.getCantidad());
                response.put("msg", "Se actualizó la cantidad de '" + producto.getNombre() + "' en el carrito");
                response.put("tipo", "info");
            } else {
                DetalleOrden detalleOrden = new DetalleOrden();
                detalleOrden.setCantidad(cantidad);
                detalleOrden.setPrecio(producto.getPrecio());
                detalleOrden.setNombre(producto.getNombre());
                detalleOrden.setTotal(producto.getPrecio() * cantidad);
                detalleOrden.setProducto(producto);
                detalles.add(detalleOrden);
                response.put("msg", "'" + producto.getNombre() + "' agregado al carrito");
                response.put("tipo", "success");
            }

            double sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
            orden.setTotal(sumaTotal);

        } catch (Exception e) {
            response.put("msg", "Error al agregar el producto");
            response.put("tipo", "danger");
        }

        return ResponseEntity.ok(response);
    }

    //quitar producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model){
    
        //lista nueva de productos
        List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
        for(DetalleOrden detalleOrden: detalles){
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNuevas.add(detalleOrden);
            }
        }

        //poner la nueva lista con los productos restantes
        detalles = ordenesNuevas;

        double sumaTotal = 0;
        sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);


        return "usuario/carrito";
    }

    @GetMapping("/getCart")
    public String getCart(Model model){

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(){
        
        return "usuario/resumenorden";
    }

}
