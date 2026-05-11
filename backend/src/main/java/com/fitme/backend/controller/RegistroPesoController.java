package com.fitme.backend.controller;

import com.fitme.backend.entity.RegistroPeso;
import com.fitme.backend.repository.RegistroPesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/pesos")
@RequiredArgsConstructor
public class RegistroPesoController {

    private final RegistroPesoRepository pesoRepo;

    @GetMapping
    public List<RegistroPeso> getPesos(@PathVariable Integer usuarioId) {
        return pesoRepo.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    @PostMapping
    public RegistroPeso addPeso(@PathVariable Integer usuarioId, @RequestBody RegistroPeso peso) {
        peso.setId(null);
        peso.setUsuarioId(usuarioId);
        return pesoRepo.save(peso);
    }

    @GetMapping("/ultimo")
    public ResponseEntity<RegistroPeso> getUltimo(@PathVariable Integer usuarioId) {
        return pesoRepo.findTopByUsuarioIdOrderByFechaDesc(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
