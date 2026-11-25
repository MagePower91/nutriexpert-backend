package cl.duoc.nutriexpert.catalogo.service;

import cl.duoc.nutriexpert.catalogo.exception.ConflictoException;
import cl.duoc.nutriexpert.catalogo.exception.RecursoNoEncontradoException;
import cl.duoc.nutriexpert.catalogo.model.Cultivo;
import cl.duoc.nutriexpert.catalogo.repository.CultivoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CultivoService {

    private final CultivoRepository repository;

    public CultivoService(CultivoRepository repository) {
        this.repository = repository;
    }

    public List<Cultivo> listar() {
        return repository.findAll(Sort.by("id"));
    }

    public Cultivo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cultivo con id " + id));
    }

    public Cultivo crear(Cultivo cultivo) {
        String nombre = cultivo.getNombre().trim();
        if (repository.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictoException("Ya existe un cultivo llamado " + nombre);
        }
        cultivo.setId(null); // el id lo pone la base de datos
        cultivo.setNombre(nombre);
        return repository.save(cultivo);
    }
}
