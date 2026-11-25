package cl.duoc.nutriexpert.catalogo.controller;

import cl.duoc.nutriexpert.catalogo.model.Cultivo;
import cl.duoc.nutriexpert.catalogo.service.CultivoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cultivos")
public class CultivoController {

    private final CultivoService service;

    public CultivoController(CultivoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cultivo> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Cultivo buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cultivo crear(@Valid @RequestBody Cultivo cultivo) {
        return service.crear(cultivo);
    }
}
