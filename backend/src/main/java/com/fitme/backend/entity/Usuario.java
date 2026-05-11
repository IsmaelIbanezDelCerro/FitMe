package com.fitme.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "peso_actual")
    private Float pesoActual;

    @Column(name = "altura_cm")
    private Float alturaCm;

    @Column(name = "imc_actual")
    private Float imcActual;
}
