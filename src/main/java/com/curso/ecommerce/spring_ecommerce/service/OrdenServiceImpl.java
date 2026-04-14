package com.curso.ecommerce.spring_ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.spring_ecommerce.model.Orden;
import com.curso.ecommerce.spring_ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

    @Autowired
    private IOrdenRepository ordenRepository;

    @Override
    public Orden save(Orden orden) {
        return ordenRepository.save(orden);
    }

}