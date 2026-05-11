package com.fitme.backend.repository;

import com.fitme.backend.entity.PreferenciaAlimenticia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreferenciaAlimenticiaRepository extends JpaRepository<PreferenciaAlimenticia, Integer> {
    List<PreferenciaAlimenticia> findByUsuarioId(Integer usuarioId);
}
