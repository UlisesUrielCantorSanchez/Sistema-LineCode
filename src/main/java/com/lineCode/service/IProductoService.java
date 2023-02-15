package com.lineCode.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lineCode.model.Producto;

public interface IProductoService {
	
    public List<Producto> findAll();
	
	public Page<Producto> findAll(Pageable pageable);
	
	public Optional<Producto> get(Integer id);
	
	public void save(Producto producto);

	public Producto findOne(Integer id);

	public void delete(Integer id);
	
	public void update(Producto producto);

}
