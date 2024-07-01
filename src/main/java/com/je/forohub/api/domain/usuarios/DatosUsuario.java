package com.je.forohub.api.domain.usuarios;

import jakarta.validation.constraints.NotNull;

public record DatosUsuario(Long id, @NotNull String usuario, @NotNull String clave) {
	public DatosUsuario(Usuario usuario) {
		this(usuario.getId(), usuario.getUsuario(), usuario.getClave());
	}
}
