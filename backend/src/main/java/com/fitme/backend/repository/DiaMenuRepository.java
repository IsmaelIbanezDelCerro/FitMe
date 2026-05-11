package com.fitme.backend.repository;

import com.fitme.backend.entity.DiaMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface DiaMenuRepository extends JpaRepository<DiaMenu, Integer> {
    List<DiaMenu> findByMenuIdOrderByDiaSemana(Integer menuId);

    @Transactional
    void deleteByMenuId(Integer menuId);
}
