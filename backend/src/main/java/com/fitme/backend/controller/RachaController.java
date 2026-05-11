package com.fitme.backend.controller;

import com.fitme.backend.entity.Racha;
import com.fitme.backend.repository.RachaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/racha")
@RequiredArgsConstructor
public class RachaController {

    private final RachaRepository rachaRepo;

    @GetMapping
    public ResponseEntity<Racha> getRacha(@PathVariable Integer usuarioId) {
        return rachaRepo.findByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public Racha updateRacha(@PathVariable Integer usuarioId, @RequestBody Racha datos) {
        return rachaRepo.findByUsuarioId(usuarioId).map(r -> {
            r.setDiasConsecutivos(datos.getDiasConsecutivos());
            r.setUltimaActividad(datos.getUltimaActividad());
            if (datos.getRachaMaxima() != null && datos.getRachaMaxima() > r.getRachaMaxima()) {
                r.setRachaMaxima(datos.getRachaMaxima());
            }
            return rachaRepo.save(r);
        }).orElseGet(() -> {
            datos.setId(null);
            datos.setUsuarioId(usuarioId);
            if (datos.getRachaMaxima() == null) datos.setRachaMaxima(0);
            if (datos.getDiasConsecutivos() == null) datos.setDiasConsecutivos(0);
            return rachaRepo.save(datos);
        });
    }
}
