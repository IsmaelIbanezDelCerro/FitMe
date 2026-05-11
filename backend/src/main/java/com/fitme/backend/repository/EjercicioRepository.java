package com.fitme.backend.repository;

import com.fitme.backend.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    List<Ejercicio> findByRutinaId(Integer rutinaId);

    @Transactional
    void deleteByRutinaId(Integer rutinaId);
}
