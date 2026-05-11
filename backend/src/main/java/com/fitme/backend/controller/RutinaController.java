package com.fitme.backend.controller;

import com.fitme.backend.entity.Ejercicio;
import com.fitme.backend.entity.Rutina;
import com.fitme.backend.repository.EjercicioRepository;
import com.fitme.backend.repository.RutinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaRepository rutinaRepo;
    private final EjercicioRepository ejercicioRepo;

    @GetMapping("/api/usuarios/{usuarioId}/rutinas")
    public List<Rutina> getRutinas(@PathVariable Integer usuarioId) {
        return rutinaRepo.findByUsuarioId(usuarioId);
    }

    @PostMapping("/api/usuarios/{usuarioId}/rutinas")
    public Rutina addRutina(@PathVariable Integer usuarioId, @RequestBody Rutina rutina) {
        rutina.setId(null);
        rutina.setUsuarioId(usuarioId);
        if (rutina.getActiva() == null) rutina.setActiva(true);
        return rutinaRepo.save(rutina);
    }

    @DeleteMapping("/api/rutinas/{id}")
    public ResponseEntity<Void> deleteRutina(@PathVariable Integer id) {
        ejercicioRepo.deleteByRutinaId(id);
        rutinaRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/rutinas/{rutinaId}/ejercicios")
    public List<Ejercicio> getEjercicios(@PathVariable Integer rutinaId) {
        return ejercicioRepo.findByRutinaId(rutinaId);
    }

    @PostMapping("/api/rutinas/{rutinaId}/ejercicios")
    public Ejercicio addEjercicio(@PathVariable Integer rutinaId, @RequestBody Ejercicio ejercicio) {
        ejercicio.setId(null);
        ejercicio.setRutinaId(rutinaId);
        return ejercicioRepo.save(ejercicio);
    }

    @PutMapping("/api/ejercicios/{id}")
    public ResponseEntity<Ejercicio> updateEjercicio(@PathVariable Integer id, @RequestBody Ejercicio datos) {
        return ejercicioRepo.findById(id).map(e -> {
            if (datos.getNombre() != null) e.setNombre(datos.getNombre());
            if (datos.getGrupoMuscular() != null) e.setGrupoMuscular(datos.getGrupoMuscular());
            if (datos.getSeries() != null) e.setSeries(datos.getSeries());
            if (datos.getRepeticiones() != null) e.setRepeticiones(datos.getRepeticiones());
            if (datos.getDescansoSeg() != null) e.setDescansoSeg(datos.getDescansoSeg());
            return ResponseEntity.ok(ejercicioRepo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/ejercicios/{id}")
    public ResponseEntity<Void> deleteEjercicio(@PathVariable Integer id) {
        ejercicioRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
