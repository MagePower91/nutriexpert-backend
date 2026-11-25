package cl.duoc.nutriexpert.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroUsuarioRequest(
        @NotBlank(message = "nombre es obligatorio")
        String nombre,

        @NotBlank(message = "correo es obligatorio")
        @Email(message = "correo no tiene un formato valido")
        String correo,

        @NotBlank(message = "rol es obligatorio")
        @Pattern(regexp = "AGRICULTOR|ASESOR", message = "rol debe ser AGRICULTOR o ASESOR")
        String rol,

        // BCrypt solo usa los primeros 72 bytes, por eso el maximo.
        @NotBlank(message = "clave es obligatoria")
        @Size(min = 8, max = 72, message = "clave debe tener entre 8 y 72 caracteres")
        String clave
) { }
