package com.lineCode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineCode.model.Orden;
import com.lineCode.model.Usuario;

public interface OrdenRepository extends JpaRepository<Orden, Integer>{
	
	List<Orden> findByUsuario (Usuario usuario);

}
