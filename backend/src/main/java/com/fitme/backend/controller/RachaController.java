package com.fitme.backend.controller;

import com.fitme.backend.entity.Racha;
import com.fitme.backend.repository.RachaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/racha")
@RequiredArgsConstructor
public class RachaController {

    private final RachaRepository rachaRepo;

    @GetMapping
    public Racha getRacha(@PathVariable Integer usuarioId) {
        return rachaRepo.findByUsuarioId(usuarioId).orElseGet(() -> {
            Racha r = new Racha();
            r.setUsuarioId(usuarioId);
            r.setDiasConsecutivos(0);
            r.setRachaMaxima(0);
            return r;
        });
    }

    @PostMapping("/check")
    public Racha registrarCheck(@PathVariable Integer usuarioId) {
        LocalDate hoy = LocalDate.now();

        Racha r = rachaRepo.findByUsuarioId(usuarioId).orElseGet(() -> {
            Racha nueva = new Racha();
            nueva.setUsuarioId(usuarioId);
            nueva.setDiasConsecutivos(0);
            nueva.setRachaMaxima(0);
            return nueva;
        });

        if (hoy.equals(r.getUltimaActividad())) {
            return r;
        }

        if (r.getUltimaActividad() != null && hoy.minusDays(1).equals(r.getUltimaActividad())) {
            r.setDiasConsecutivos(r.getDiasConsecutivos() + 1);
        } else {
            r.setDiasConsecutivos(1);
        }

        r.setUltimaActividad(hoy);

        if (r.getRachaMaxima() == null || r.getDiasConsecutivos() > r.getRachaMaxima()) {
            r.setRachaMaxima(r.getDiasConsecutivos());
        }

        return rachaRepo.save(r);
    }
}
