package cl.duoc.nutriexpert.registros.service;

import cl.duoc.nutriexpert.registros.client.CatalogoClient;
import cl.duoc.nutriexpert.registros.exception.DatoInvalidoException;
import cl.duoc.nutriexpert.registros.exception.RecursoNoEncontradoException;
import cl.duoc.nutriexpert.registros.model.Registro;
import cl.duoc.nutriexpert.registros.repository.RegistroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

    @Mock
    private RegistroRepository repository;

    @Mock
    private CatalogoClient catalogoClient;

    @InjectMocks
    private RegistroService service;

    private Registro registro() {
        Registro r = new Registro();
        r.setIdLocal(7L);
        r.setUsuarioId(1L);
        r.setCultivo("Palto");
        r.setLote(" L-01 ");
        r.setMetaRendimiento(12.0);
        r.setEficienciaRiego(85.0);
        r.setN(84.7);
        r.setP2o5(28.2);
        r.setK2o(112.9);
        r.setCao(42.4);
        r.setMgo(21.2);
        r.setCreadoEn(1795600000000L);
        return r;
    }

    @Test
    void guardarNuevoLoCreaYLimpiaElLote() {
        when(repository.findByUsuarioIdAndIdLocal(1L, 7L)).thenReturn(Optional.empty());
        when(catalogoClient.existeCultivo("Palto")).thenReturn(Optional.of(true));
        when(repository.save(any(Registro.class))).thenAnswer(inv -> {
            Registro r = inv.getArgument(0);
            r.setId(15L);
            return r;
        });

        RegistroService.ResultadoGuardar resultado = service.guardar(registro());

        assertTrue(resultado.creado());
        assertEquals(15L, resultado.registro().getId());
        assertEquals("L-01", resultado.registro().getLote());
    }

    @Test
    void guardarRepetidoDevuelveElExistenteSinDuplicar() {
        Registro existente = registro();
        existente.setId(15L);
        when(repository.findByUsuarioIdAndIdLocal(1L, 7L)).thenReturn(Optional.of(existente));

        RegistroService.ResultadoGuardar resultado = service.guardar(registro());

        assertFalse(resultado.creado());
        assertSame(existente, resultado.registro());
        verify(repository, never()).save(any());
    }

    @Test
    void guardarConCultivoQueNoEstaEnElCatalogoLanzaError() {
        Registro r = registro();
        r.setCultivo("Frutilla");
        when(repository.findByUsuarioIdAndIdLocal(1L, 7L)).thenReturn(Optional.empty());
        when(catalogoClient.existeCultivo("Frutilla")).thenReturn(Optional.of(false));

        assertThrows(DatoInvalidoException.class, () -> service.guardar(r));
        verify(repository, never()).save(any());
    }

    @Test
    void guardarSinRespuestaDelCatalogoAceptaElRegistro() {
        when(repository.findByUsuarioIdAndIdLocal(1L, 7L)).thenReturn(Optional.empty());
        when(catalogoClient.existeCultivo("Palto")).thenReturn(Optional.empty());
        when(repository.save(any(Registro.class))).thenAnswer(inv -> inv.getArgument(0));

        assertTrue(service.guardar(registro()).creado());
    }

    @Test
    void listarPorUsuarioDelegaEnElRepositorio() {
        when(repository.findByUsuarioIdOrderByCreadoEnDesc(1L)).thenReturn(List.of(registro()));

        assertEquals(1, service.listarPorUsuario(1L).size());
    }

    @Test
    void borrarInexistenteLanza404() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class, () -> service.borrar(99L));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void borrarExistenteLoElimina() {
        when(repository.existsById(15L)).thenReturn(true);

        service.borrar(15L);

        verify(repository).deleteById(15L);
    }
}
