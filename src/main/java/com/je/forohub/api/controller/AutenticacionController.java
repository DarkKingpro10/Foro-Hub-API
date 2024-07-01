package com.je.forohub.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.je.forohub.api.domain.usuarios.DatosAutenticacionUsuario;
import com.je.forohub.api.domain.usuarios.Usuario;
import com.je.forohub.api.infra.security.TokenService;
import com.je.forohub.api.infra.security.DatosJWTToken;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("login")
@Tag(name = "Autenticacion", description = "Obtiene el token para el usuario asignado que da acceso al resto de endpoint")
public class AutenticacionController {
	
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    
    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService) {
    	this.authenticationManager = authenticationManager;
    	this.tokenService = tokenService;
    }
    
    @PostMapping
    public ResponseEntity login(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) {
    	Authentication authToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.usuario(),
                datosAutenticacionUsuario.clave());
    	var usuarioAutenticado = authenticationManager.authenticate(authToken);
    	 var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
    	 return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
    }
}
