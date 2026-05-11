package com.fitme.backend.controller;

import com.fitme.backend.entity.PreferenciaAlimenticia;
import com.fitme.backend.repository.PreferenciaAlimenticiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PreferenciaController {

    private final PreferenciaAlimenticiaRepository prefRepo;

    @GetMapping("/usuarios/{usuarioId}/preferencias")
    public List<PreferenciaAlimenticia> getPreferencias(@PathVariable Integer usuarioId) {
        return prefRepo.findByUsuarioId(usuarioId);
    }

    @PostMapping("/usuarios/{usuarioId}/preferencias")
    public PreferenciaAlimenticia addPreferencia(@PathVariable Integer usuarioId, @RequestBody PreferenciaAlimenticia pref) {
        pref.setId(null);
        pref.setUsuarioId(usuarioId);
        return prefRepo.save(pref);
    }

    @DeleteMapping("/preferencias/{id}")
    public ResponseEntity<Void> deletePreferencia(@PathVariable Integer id) {
        prefRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
