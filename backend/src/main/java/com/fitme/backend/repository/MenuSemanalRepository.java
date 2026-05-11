package com.fitme.backend.repository;

import com.fitme.backend.entity.MenuSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuSemanalRepository extends JpaRepository<MenuSemanal, Integer> {
    List<MenuSemanal> findByUsuarioIdOrderBySemanaInicioDesc(Integer usuarioId);
}
