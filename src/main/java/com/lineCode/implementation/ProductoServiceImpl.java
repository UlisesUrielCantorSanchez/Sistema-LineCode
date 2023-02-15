package com.lineCode.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.lineCode.model.Producto;
import com.lineCode.repository.ProductoRepository;
import com.lineCode.service.IProductoService;
@Service
public class ProductoServiceImpl implements IProductoService{
	
	@Autowired
	private ProductoRepository productoRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		return (List<Producto>) productoRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAll(Pageable pageable) {
		return productoRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Producto empleado) {
		productoRepository.save(empleado);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		productoRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findOne(Integer id) {
		return productoRepository.findById(id).orElse(null);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		productoRepository.save(producto);		
	}
}
