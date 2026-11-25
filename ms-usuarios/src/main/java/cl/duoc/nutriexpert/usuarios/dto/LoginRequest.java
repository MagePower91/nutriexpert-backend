package cl.duoc.nutriexpert.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "correo es obligatorio")
        String correo,

        @NotBlank(message = "clave es obligatoria")
        String clave
) { }
