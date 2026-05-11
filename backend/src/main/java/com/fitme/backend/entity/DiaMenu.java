package com.fitme.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dia_menu")
@Data
@NoArgsConstructor
public class DiaMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "dia_semana", columnDefinition = "TINYINT")
    private Integer diaSemana;

    private String desayuno;
    private String almuerzo;
    private String cena;

    @Column(name = "kcal_totales")
    private Integer kcalTotales;
}
