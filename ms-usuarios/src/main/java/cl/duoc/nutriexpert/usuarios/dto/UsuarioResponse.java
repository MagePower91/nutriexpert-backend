package cl.duoc.nutriexpert.usuarios.dto;

import cl.duoc.nutriexpert.usuarios.model.Usuario;

// Lo unico que sale del servicio sobre un usuario: sin la clave ni su hash.
public record UsuarioResponse(Long id, String nombre, String correo, String rol) {

    public static UsuarioResponse desde(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getCorreo(), u.getRol());
    }
}
