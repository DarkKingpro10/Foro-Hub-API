package com.je.forohub.api.domain.usuarios;

import jakarta.validation.constraints.NotNull;

public record DatosAutenticacionUsuario(@NotNull String usuario, @NotNull String clave) {}