package com.lineCode.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lineCode.model.DetalleOrden;
import com.lineCode.repository.DetalleOrdenRepository;
import com.lineCode.service.IDetalleOrdenService;

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService{
	
	@Autowired
	private DetalleOrdenRepository detalleOrdenRepository;

	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		return detalleOrdenRepository.save(detalleOrden);
	}

}
