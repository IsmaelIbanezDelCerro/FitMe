package com.fitme.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gimnasio_cercano")
@Data
@NoArgsConstructor
public class GimnasioCercano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    private String nombre;
    private String direccion;

    @Column(name = "distancia_km")
    private Float distanciaKm;

    private Float valoracion;
}
