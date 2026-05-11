package com.fitme.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "registro_peso")
@Data
@NoArgsConstructor
public class RegistroPeso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "peso_kg")
    private Float pesoKg;

    private LocalDate fecha;

    private String nota;
}
