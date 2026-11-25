package cl.duoc.nutriexpert.usuarios.service;

import cl.duoc.nutriexpert.usuarios.dto.LoginRequest;
import cl.duoc.nutriexpert.usuarios.dto.RegistroUsuarioRequest;
import cl.duoc.nutriexpert.usuarios.dto.UsuarioResponse;
import cl.duoc.nutriexpert.usuarios.exception.CorreoExistenteException;
import cl.duoc.nutriexpert.usuarios.exception.CredencialesInvalidasException;
import cl.duoc.nutriexpert.usuarios.exception.RecursoNoEncontradoException;
import cl.duoc.nutriexpert.usuarios.model.Usuario;
import cl.duoc.nutriexpert.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    // BCrypt real con costo bajo para que los tests sean rapidos.
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(repository, encoder);
    }

    @Test
    void registrarGuardaCorreoEnMinusculasYClaveConHash() {
        when(repository.existsByCorreo("demo@nutriexpert.cl")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponse resp = service.registrar(
                new RegistroUsuarioRequest(" Usuario Demo ", " Demo@NutriExpert.cl ", "AGRICULTOR", "ClaveDemo123"));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        Usuario guardado = captor.getValue();
        assertEquals("demo@nutriexpert.cl", guardado.getCorreo());
        assertNotEquals("ClaveDemo123", guardado.getClaveHash());
        assertTrue(encoder.matches("ClaveDemo123", guardado.getClaveHash()));
        assertEquals(1L, resp.id());
        assertEquals("Usuario Demo", resp.nombre());
    }

    @Test
    void registrarCorreoRepetidoLanzaConflicto() {
        when(repository.existsByCorreo("demo@nutriexpert.cl")).thenReturn(true);

        assertThrows(CorreoExistenteException.class, () -> service.registrar(
                new RegistroUsuarioRequest("Otro", "demo@nutriexpert.cl", "ASESOR", "ClaveDemo123")));
        verify(repository, never()).save(any());
    }

    @Test
    void loginCorrectoDevuelveUsuarioSinClave() {
        Usuario u = new Usuario(1L, "Usuario Demo", "demo@nutriexpert.cl", "AGRICULTOR",
                encoder.encode("ClaveDemo123"));
        when(repository.findByCorreo("demo@nutriexpert.cl")).thenReturn(Optional.of(u));

        UsuarioResponse resp = service.login(new LoginRequest("DEMO@nutriexpert.cl", "ClaveDemo123"));

        assertEquals(1L, resp.id());
        assertEquals("AGRICULTOR", resp.rol());
    }

    @Test
    void loginConClaveIncorrectaLanzaCredencialesInvalidas() {
        Usuario u = new Usuario(1L, "Usuario Demo", "demo@nutriexpert.cl", "AGRICULTOR",
                encoder.encode("ClaveDemo123"));
        when(repository.findByCorreo("demo@nutriexpert.cl")).thenReturn(Optional.of(u));

        CredencialesInvalidasException e = assertThrows(CredencialesInvalidasException.class,
                () -> service.login(new LoginRequest("demo@nutriexpert.cl", "otraClave1")));
        assertEquals("Correo o clave incorrectos", e.getMessage());
    }

    @Test
    void loginConCorreoInexistenteDaElMismoMensaje() {
        when(repository.findByCorreo("nadie@nutriexpert.cl")).thenReturn(Optional.empty());

        CredencialesInvalidasException e = assertThrows(CredencialesInvalidasException.class,
                () -> service.login(new LoginRequest("nadie@nutriexpert.cl", "ClaveDemo123")));
        assertEquals("Correo o clave incorrectos", e.getMessage());
    }

    @Test
    void buscarPorIdInexistenteLanza404() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.buscarPorId(99L));
    }
}
