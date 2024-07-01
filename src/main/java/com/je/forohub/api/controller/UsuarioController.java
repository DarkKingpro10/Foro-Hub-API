package com.je.forohub.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.je.forohub.api.domain.usuarios.DatosActualizarUsuario;
import com.je.forohub.api.domain.usuarios.DatosListadoUsuario;
import com.je.forohub.api.domain.usuarios.DatosUsuario;
import com.je.forohub.api.domain.usuarios.Usuario;
import com.je.forohub.api.domain.usuarios.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping
	@Transactional
	@Operation(summary = "Registra un nuevo usuario en la Base de Datos")
	public ResponseEntity<DatosUsuario> registrarUsuario(@RequestBody @Valid DatosUsuario datos,
			UriComponentsBuilder uriComponentsBuilder) {
		// Encriptar la contraseña
		String claveEncriptada = passwordEncoder.encode(datos.clave());

		// Crear un nuevo objeto Usuario con la contraseña encriptada
		Usuario usuario = new Usuario(null, datos.usuario(), claveEncriptada);

		// Guardar el usuario en la base de datos
		usuario = usuarioRepository.save(usuario);

		// Preparar la respuesta con los datos del usuario registrado
		DatosUsuario res = new DatosUsuario(usuario.getId(), usuario.getUsuario(), usuario.getClave());

		// Construir la URI para la respuesta
		URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

		// Devolver respuesta con estado 201 Created y la URI del nuevo usuario
		return ResponseEntity.created(url).body(res);
	}

	@GetMapping
	@Operation(summary = "Obtiene el listado de usuarios")
	public ResponseEntity<Page<DatosListadoUsuario>> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion) {
//      return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
		return ResponseEntity.ok(usuarioRepository.findAll(paginacion).map(DatosListadoUsuario::new));
	}
	
	@PutMapping
	@Transactional
	@Operation(summary = "Actualiza un usuario")
	public ResponseEntity actualizarUsuario(@RequestBody @Valid DatosActualizarUsuario datos) {
		if(datos.id() == null || !usuarioRepository.existsById(datos.id())) {
			throw new ValidationException("No existe usuario con ese id");
		}
		
		var usuario = usuarioRepository.getReferenceById(datos.id());
		
		// Encriptar la contraseña
		String claveEncriptada =  datos.clave() != null ? passwordEncoder.encode(datos.clave()) : usuario.getClave();
		
		usuario.setUsuario(datos.usuario());
		
		usuario.setClave(claveEncriptada);
		
		DatosUsuario res = new DatosUsuario(usuario.getId(), usuario.getUsuario(), usuario.getClave());
		return ResponseEntity.ok(res);
	}
}
