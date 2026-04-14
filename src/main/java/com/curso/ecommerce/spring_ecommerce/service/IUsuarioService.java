package com.curso.ecommerce.spring_ecommerce.service;

import java.util.Optional;

import com.curso.ecommerce.spring_ecommerce.model.Usuario;

public interface IUsuarioService {

    Optional<Usuario> findById(Integer id);
}
