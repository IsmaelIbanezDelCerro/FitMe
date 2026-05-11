package com.fitme.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "racha")
@Data
@NoArgsConstructor
public class Racha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", unique = true)
    private Integer usuarioId;

    @Column(name = "dias_consecutivos")
    private Integer diasConsecutivos;

    @Column(name = "ultima_actividad")
    private LocalDate ultimaActividad;

    @Column(name = "racha_maxima")
    private Integer rachaMaxima;
}
