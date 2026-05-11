package com.fitme.backend.repository;

import com.fitme.backend.entity.RegistroPeso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegistroPesoRepository extends JpaRepository<RegistroPeso, Integer> {
    List<RegistroPeso> findByUsuarioIdOrderByFechaDesc(Integer usuarioId);
    Optional<RegistroPeso> findTopByUsuarioIdOrderByFechaDesc(Integer usuarioId);
}
