package com.example.backendintegrador.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "agencia")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agencia")
    private Integer idAgencia;

    @NotBlank(message = "El departamento es obligatorio")
    @Column(name = "departamento", nullable = false)
    private String departamento;

    @NotBlank(message = "La provincia es obligatoria")
    @Column(name = "provincia", nullable = false)
    private String provincia;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "referencia")
    private String referencia;

    @OneToMany(mappedBy = "agencia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // RutaAgencias son el lado "hijo"
    private Set<RutaAgencia> rutaAgencias;
}
