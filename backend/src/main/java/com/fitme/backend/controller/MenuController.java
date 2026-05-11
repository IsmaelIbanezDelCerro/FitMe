package com.fitme.backend.controller;

import com.fitme.backend.entity.DiaMenu;
import com.fitme.backend.entity.MenuSemanal;
import com.fitme.backend.repository.DiaMenuRepository;
import com.fitme.backend.repository.MenuSemanalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuSemanalRepository menuRepo;
    private final DiaMenuRepository diaRepo;

    @GetMapping("/api/usuarios/{usuarioId}/menus")
    public List<MenuSemanal> getMenus(@PathVariable Integer usuarioId) {
        return menuRepo.findByUsuarioIdOrderBySemanaInicioDesc(usuarioId);
    }

    @PostMapping("/api/usuarios/{usuarioId}/menus")
    public MenuSemanal addMenu(@PathVariable Integer usuarioId, @RequestBody MenuSemanal menu) {
        menu.setId(null);
        menu.setUsuarioId(usuarioId);
        menu.setGeneradoEn(LocalDateTime.now());
        return menuRepo.save(menu);
    }

    @DeleteMapping("/api/menus/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Integer id) {
        diaRepo.deleteByMenuId(id);
        menuRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/menus/{menuId}/dias")
    public List<DiaMenu> getDias(@PathVariable Integer menuId) {
        return diaRepo.findByMenuIdOrderByDiaSemana(menuId);
    }

    @PostMapping("/api/menus/{menuId}/dias")
    public DiaMenu addDia(@PathVariable Integer menuId, @RequestBody DiaMenu dia) {
        dia.setId(null);
        dia.setMenuId(menuId);
        return diaRepo.save(dia);
    }

    @PutMapping("/api/dias/{id}")
    public ResponseEntity<DiaMenu> updateDia(@PathVariable Integer id, @RequestBody DiaMenu datos) {
        return diaRepo.findById(id).map(d -> {
            if (datos.getDesayuno() != null) d.setDesayuno(datos.getDesayuno());
            if (datos.getAlmuerzo() != null) d.setAlmuerzo(datos.getAlmuerzo());
            if (datos.getCena() != null) d.setCena(datos.getCena());
            if (datos.getKcalTotales() != null) d.setKcalTotales(datos.getKcalTotales());
            return ResponseEntity.ok(diaRepo.save(d));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/dias/{id}")
    public ResponseEntity<Void> deleteDia(@PathVariable Integer id) {
        diaRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
