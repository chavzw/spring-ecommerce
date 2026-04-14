package com.curso.ecommerce.spring_ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.spring_ecommerce.model.DetalleOrden;

@Repository
public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {

}
