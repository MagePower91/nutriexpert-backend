package cl.duoc.nutriexpert.registros.repository;

import cl.duoc.nutriexpert.registros.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistroRepository extends JpaRepository<Registro, Long> {
    Optional<Registro> findByUsuarioIdAndIdLocal(Long usuarioId, Long idLocal);

    List<Registro> findByUsuarioIdOrderByCreadoEnDesc(Long usuarioId);
}
