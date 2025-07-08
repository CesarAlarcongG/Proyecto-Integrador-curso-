package com.example.backendintegrador.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trabajador")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajador")
    private Integer idTrabajador;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El cargo es obligatorio")
    @Column(name = "cargo", nullable = false)
    private String cargo;
}
