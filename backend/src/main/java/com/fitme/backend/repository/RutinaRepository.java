package com.fitme.backend.repository;

import com.fitme.backend.entity.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RutinaRepository extends JpaRepository<Rutina, Integer> {
    List<Rutina> findByUsuarioId(Integer usuarioId);
    Optional<Rutina> findFirstByUsuarioIdAndNombre(Integer usuarioId, String nombre);
}
