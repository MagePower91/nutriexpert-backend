package cl.duoc.nutriexpert.usuarios.service;

import cl.duoc.nutriexpert.usuarios.dto.LoginRequest;
import cl.duoc.nutriexpert.usuarios.dto.RegistroUsuarioRequest;
import cl.duoc.nutriexpert.usuarios.dto.UsuarioResponse;
import cl.duoc.nutriexpert.usuarios.exception.CorreoExistenteException;
import cl.duoc.nutriexpert.usuarios.exception.CredencialesInvalidasException;
import cl.duoc.nutriexpert.usuarios.exception.RecursoNoEncontradoException;
import cl.duoc.nutriexpert.usuarios.model.Usuario;
import cl.duoc.nutriexpert.usuarios.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    // Hash de una clave cualquiera: si el correo no existe se compara igual contra este,
    // asi la respuesta tarda lo mismo y no delata que correos estan registrados.
    private final String hashFalso;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
        this.hashFalso = encoder.encode("clave-que-no-se-usa");
    }

    public UsuarioResponse registrar(RegistroUsuarioRequest req) {
        String correo = normalizar(req.correo());
        if (repository.existsByCorreo(correo)) {
            throw new CorreoExistenteException();
        }
        Usuario usuario = new Usuario(null, req.nombre().trim(), correo, req.rol(), encoder.encode(req.clave()));
        try {
            return UsuarioResponse.desde(repository.save(usuario));
        } catch (DataIntegrityViolationException e) {
            // Dos registros simultaneos con el mismo correo.
            throw new CorreoExistenteException();
        }
    }

    public UsuarioResponse login(LoginRequest req) {
        Usuario usuario = repository.findByCorreo(normalizar(req.correo())).orElse(null);
        String hash = usuario != null ? usuario.getClaveHash() : hashFalso;
        boolean coincide = encoder.matches(req.clave(), hash);
        if (usuario == null || !coincide) {
            throw new CredencialesInvalidasException();
        }
        return UsuarioResponse.desde(usuario);
    }

    public UsuarioResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(UsuarioResponse::desde)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
    }

    private static String normalizar(String correo) {
        return correo.trim().toLowerCase(Locale.ROOT);
    }
}
