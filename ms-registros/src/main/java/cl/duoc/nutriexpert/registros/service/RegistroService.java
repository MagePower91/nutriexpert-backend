package cl.duoc.nutriexpert.registros.service;

import cl.duoc.nutriexpert.registros.client.CatalogoClient;
import cl.duoc.nutriexpert.registros.exception.DatoInvalidoException;
import cl.duoc.nutriexpert.registros.exception.RecursoNoEncontradoException;
import cl.duoc.nutriexpert.registros.model.Registro;
import cl.duoc.nutriexpert.registros.repository.RegistroRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroService {

    // creado = false cuando el registro ya existia (reintento de la app).
    public record ResultadoGuardar(Registro registro, boolean creado) { }

    private final RegistroRepository repository;
    private final CatalogoClient catalogoClient;

    public RegistroService(RegistroRepository repository, CatalogoClient catalogoClient) {
        this.repository = repository;
        this.catalogoClient = catalogoClient;
    }

    public ResultadoGuardar guardar(Registro registro) {
        // Idempotencia: el mismo usuarioId + idLocal devuelve el que ya existe.
        Optional<Registro> existente =
                repository.findByUsuarioIdAndIdLocal(registro.getUsuarioId(), registro.getIdLocal());
        if (existente.isPresent()) {
            return new ResultadoGuardar(existente.get(), false);
        }

        String cultivo = registro.getCultivo().trim();
        if (catalogoClient.existeCultivo(cultivo).map(existe -> !existe).orElse(false)) {
            throw new DatoInvalidoException("El cultivo " + cultivo + " no existe en el catalogo");
        }

        registro.setId(null); // el id lo pone la base de datos
        registro.setCultivo(cultivo);
        registro.setLote(registro.getLote().trim());
        try {
            return new ResultadoGuardar(repository.save(registro), true);
        } catch (DataIntegrityViolationException e) {
            // Dos envios simultaneos del mismo registro: gano el otro, se devuelve ese.
            return repository.findByUsuarioIdAndIdLocal(registro.getUsuarioId(), registro.getIdLocal())
                    .map(r -> new ResultadoGuardar(r, false))
                    .orElseThrow(() -> e);
        }
    }

    public List<Registro> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdOrderByCreadoEnDesc(usuarioId);
    }

    public Registro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el registro con id " + id));
    }

    // Actualiza los datos editables. id, idLocal, usuarioId y creadoEn no cambian:
    // identifican al registro y a su dueño.
    public Registro actualizar(Long id, Registro datos) {
        Registro actual = buscarPorId(id);

        String cultivo = datos.getCultivo().trim();
        if (catalogoClient.existeCultivo(cultivo).map(existe -> !existe).orElse(false)) {
            throw new DatoInvalidoException("El cultivo " + cultivo + " no existe en el catalogo");
        }

        actual.setCultivo(cultivo);
        actual.setLote(datos.getLote().trim());
        actual.setMetaRendimiento(datos.getMetaRendimiento());
        actual.setEficienciaRiego(datos.getEficienciaRiego());
        actual.setN(datos.getN());
        actual.setP2o5(datos.getP2o5());
        actual.setK2o(datos.getK2o());
        actual.setCao(datos.getCao());
        actual.setMgo(datos.getMgo());
        return repository.save(actual);
    }

    public void borrar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe el registro con id " + id);
        }
        repository.deleteById(id);
    }
}
