package com.fitme.backend.repository;

import com.fitme.backend.entity.Racha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RachaRepository extends JpaRepository<Racha, Integer> {
    Optional<Racha> findByUsuarioId(Integer usuarioId);
}
