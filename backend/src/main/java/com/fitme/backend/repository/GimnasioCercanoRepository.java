package com.fitme.backend.repository;

import com.fitme.backend.entity.GimnasioCercano;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GimnasioCercanoRepository extends JpaRepository<GimnasioCercano, Integer> {
    List<GimnasioCercano> findByUsuarioId(Integer usuarioId);
}
