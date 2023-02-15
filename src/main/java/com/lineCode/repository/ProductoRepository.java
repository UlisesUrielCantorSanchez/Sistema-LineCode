package com.lineCode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineCode.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{

}
