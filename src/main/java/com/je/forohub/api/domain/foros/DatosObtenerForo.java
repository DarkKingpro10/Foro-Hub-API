package com.je.forohub.api.domain.foros;

import org.springframework.data.domain.Page;

import com.je.forohub.api.domain.respuestas.DatosRespuesta;

public record DatosObtenerForo(DatosListarForo foro, Page<DatosRespuesta> respuestas){
	
}
