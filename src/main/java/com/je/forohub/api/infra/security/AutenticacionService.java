package com.je.forohub.api.infra.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.je.forohub.api.domain.usuarios.UsuarioRepository;


@Service
public class AutenticacionService implements UserDetailsService{
	private final UsuarioRepository usuarioRepository;
	
	public AutenticacionService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsuario(username);
    }
}
