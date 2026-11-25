package cl.duoc.nutriexpert.catalogo.service;

import cl.duoc.nutriexpert.catalogo.exception.ConflictoException;
import cl.duoc.nutriexpert.catalogo.exception.RecursoNoEncontradoException;
import cl.duoc.nutriexpert.catalogo.model.Cultivo;
import cl.duoc.nutriexpert.catalogo.repository.CultivoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CultivoServiceTest {

    @Mock
    private CultivoRepository repository;

    @InjectMocks
    private CultivoService service;

    private Cultivo palto() {
        return new Cultivo(1L, "Palto", 10.0, 60.0, 20.0, 80.0, 30.0, 15.0);
    }

    @Test
    void listarDevuelveLosCultivosDelRepositorio() {
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(palto()));

        List<Cultivo> lista = service.listar();

        assertEquals(1, lista.size());
        assertEquals("Palto", lista.get(0).getNombre());
    }

    @Test
    void buscarPorIdExistenteDevuelveElCultivo() {
        when(repository.findById(1L)).thenReturn(Optional.of(palto()));

        assertEquals(60.0, service.buscarPorId(1L).getN());
    }

    @Test
    void buscarPorIdInexistenteLanza404() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void crearIgnoraElIdEnviadoYLimpiaElNombre() {
        Cultivo nuevo = new Cultivo(50L, "  Uva ", 20.0, 90.0, 30.0, 120.0, 60.0, 20.0);
        when(repository.existsByNombreIgnoreCase("Uva")).thenReturn(false);
        when(repository.save(any(Cultivo.class))).thenAnswer(inv -> inv.getArgument(0));

        Cultivo guardado = service.crear(nuevo);

        assertNull(guardado.getId());
        assertEquals("Uva", guardado.getNombre());
    }

    @Test
    void crearConNombreRepetidoLanzaConflicto() {
        when(repository.existsByNombreIgnoreCase("Palto")).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.crear(palto()));
        verify(repository, never()).save(any());
    }
}
