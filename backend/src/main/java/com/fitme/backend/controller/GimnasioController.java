package com.fitme.backend.controller;

import com.fitme.backend.entity.GimnasioCercano;
import com.fitme.backend.repository.GimnasioCercanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GimnasioController {

    private final GimnasioCercanoRepository gimnasioRepo;

    @GetMapping("/usuarios/{usuarioId}/gimnasios")
    public List<GimnasioCercano> getGimnasios(@PathVariable Integer usuarioId) {
        return gimnasioRepo.findByUsuarioId(usuarioId);
    }

    @PostMapping("/usuarios/{usuarioId}/gimnasios")
    public GimnasioCercano addGimnasio(@PathVariable Integer usuarioId, @RequestBody GimnasioCercano gimnasio) {
        gimnasio.setId(null);
        gimnasio.setUsuarioId(usuarioId);
        return gimnasioRepo.save(gimnasio);
    }

    @DeleteMapping("/gimnasios/{id}")
    public ResponseEntity<Void> deleteGimnasio(@PathVariable Integer id) {
        gimnasioRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
