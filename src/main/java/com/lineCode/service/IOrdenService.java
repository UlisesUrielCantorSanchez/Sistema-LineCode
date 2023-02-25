package com.lineCode.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lineCode.model.Orden;
import com.lineCode.model.Usuario;

public interface IOrdenService {
	
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario (Usuario usuario);
	public Page<Orden> findAll(Pageable pageable);
	public void delete(Integer id);

}
