package com.je.forohub.api.domain.foros;

import java.time.LocalDateTime;

public record DatosListarForo(Long id, String titulo, LocalDateTime fechaCreacion, boolean estado, String autor,
		String curso) {
	public DatosListarForo(Foro foro) {
		this(foro.getId(), foro.getTitulo(), foro.getFechaCreacion(), foro.getEstado(), foro.getAutor(), foro.getCurso());
	}
}
