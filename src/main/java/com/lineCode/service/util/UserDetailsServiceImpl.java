package com.lineCode.service.util;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lineCode.model.Usuario;
import com.lineCode.service.IUsuarioService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	HttpSession session;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> optionalUser=usuarioService.findByEmail(username);
		if (optionalUser.isPresent()) {
			session.setAttribute("idusuario", optionalUser.get().getId());
			Usuario usuario= optionalUser.get();
			return User.builder().username(usuario.getNombre()).password(usuario.getPassword()).roles(usuario.getTipo()).build();
		}else {
			throw new UsernameNotFoundException("Usuario no encontrado");			
		}
	}

}
