package com.je.forohub.api.domain.usuarios;

public record DatosListadoUsuario(Long id, String usuario) {
	public DatosListadoUsuario(Usuario usuario) {
		this(usuario.getId(), usuario.getUsuario());
	}
}
