package com.je.forohub.api.domain.respuestas;

public record DatosRespuesta(Long id, String contenido, Long idTopico) {

	public DatosRespuesta(Respuesta respuesta) {
		this(respuesta.getId(), respuesta.getContenido(), respuesta.getForo().getId());
	}
}