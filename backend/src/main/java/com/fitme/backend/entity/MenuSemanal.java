package com.fitme.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu_semanal")
@Data
@NoArgsConstructor
public class MenuSemanal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "semana_inicio")
    private LocalDate semanaInicio;

    @Column(name = "semana_fin")
    private LocalDate semanaFin;

    @Column(name = "generado_en")
    private LocalDateTime generadoEn;
}
