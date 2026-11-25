package cl.duoc.nutriexpert.catalogo.repository;

import cl.duoc.nutriexpert.catalogo.model.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CultivoRepository extends JpaRepository<Cultivo, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}
