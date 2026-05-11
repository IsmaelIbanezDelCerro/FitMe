package com.fitme.backend.controller;

import com.fitme.backend.entity.Usuario;
import com.fitme.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (usuarioRepo.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email ya registrado"));
        }
        usuario.setId(null);
        usuario.setFechaRegistro(LocalDate.now());
        if (usuario.getPesoActual() != null && usuario.getAlturaCm() != null && usuario.getAlturaCm() > 0) {
            float alturaM = usuario.getAlturaCm() / 100f;
            usuario.setImcActual(usuario.getPesoActual() / (alturaM * alturaM));
        }
        return ResponseEntity.ok(usuarioRepo.save(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String nombre = credenciales.get("nombre");
        String password = credenciales.get("password");
        return usuarioRepo.findByNombreAndPassword(nombre, password)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Integer id) {
        return usuarioRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario datos) {
        return usuarioRepo.findById(id).map(u -> {
            if (datos.getNombre() != null) u.setNombre(datos.getNombre());
            if (datos.getAlturaCm() != null) u.setAlturaCm(datos.getAlturaCm());
            if (datos.getPesoActual() != null) {
                u.setPesoActual(datos.getPesoActual());
                float alturaM = (u.getAlturaCm() != null && u.getAlturaCm() > 0) ? u.getAlturaCm() / 100f : 1f;
                u.setImcActual(datos.getPesoActual() / (alturaM * alturaM));
            }
            return ResponseEntity.ok(usuarioRepo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }
}
