package com.fitme.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ejercicio")
@Data
@NoArgsConstructor
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rutina_id")
    private Integer rutinaId;

    private String nombre;

    @Column(name = "grupo_muscular")
    private String grupoMuscular;

    private Integer series;
    private Integer repeticiones;

    @Column(name = "descanso_seg")
    private Integer descansoSeg;
}
