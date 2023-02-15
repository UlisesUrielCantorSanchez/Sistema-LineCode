package com.lineCode.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lineCode.model.Orden;
import com.lineCode.model.Usuario;
import com.lineCode.repository.OrdenRepository;
import com.lineCode.service.IOrdenService;

@Service
public class OrdenServiceImpl implements IOrdenService{
	
	@Autowired
	private OrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}
	// 0000010
	public String generarNumeroOrden() {
		int numero=0;
		String numeroConcatenado="";
		
		List<Orden> ordenes = findAll();
		
		List<Integer> numeros= new ArrayList<Integer>();
		
		ordenes.stream().forEach(o -> numeros.add( Integer.parseInt( o.getNumero())));
		
		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero= numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if (numero<10) { //0000001000
			numeroConcatenado="0000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="0000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="0000"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado="0000"+String.valueOf(numero);
		}		
		
		return numeroConcatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenRepository.findById(id);
	}

}
