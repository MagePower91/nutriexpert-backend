package cl.duoc.nutriexpert.usuarios.config;

import cl.duoc.nutriexpert.usuarios.dto.RegistroUsuarioRequest;
import cl.duoc.nutriexpert.usuarios.repository.UsuarioRepository;
import cl.duoc.nutriexpert.usuarios.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Crea el usuario de demostracion del contrato JSON si no existe (datos ficticios).
// Se hace en Java y no en data.sql porque la clave debe quedar con hash BCrypt.
@Component
public class UsuarioDemoInicializador implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UsuarioDemoInicializador.class);

    private final UsuarioService service;
    private final UsuarioRepository repository;
    private final boolean crear;
    private final String correo;
    private final String clave;

    public UsuarioDemoInicializador(UsuarioService service,
                                    UsuarioRepository repository,
                                    @Value("${usuarios.demo.crear:false}") boolean crear,
                                    @Value("${usuarios.demo.correo:}") String correo,
                                    @Value("${usuarios.demo.clave:}") String clave) {
        this.service = service;
        this.repository = repository;
        this.crear = crear;
        this.correo = correo;
        this.clave = clave;
    }

    @Override
    public void run(String... args) {
        if (!crear || correo.isBlank() || repository.existsByCorreo(correo.toLowerCase())) {
            return;
        }
        service.registrar(new RegistroUsuarioRequest("Usuario Demo", correo, "AGRICULTOR", clave));
        log.info("Usuario de demostracion creado: {}", correo);
    }
}
