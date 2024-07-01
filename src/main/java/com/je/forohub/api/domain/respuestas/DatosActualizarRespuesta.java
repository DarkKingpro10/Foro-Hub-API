package com.je.forohub.api.domain.respuestas;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarRespuesta(@NotNull Long id, @NotNull String contenido) {

}
