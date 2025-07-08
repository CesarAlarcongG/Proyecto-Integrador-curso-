package com.example.backendintegrador.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "bus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carro")
    private Integer idCarro;

    @NotBlank(message = "La placa es obligatoria")
    @Column(name = "placa", nullable = false, unique = true)
    private String placa;

    @NotNull(message = "El conductor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conductor", nullable = false)
    private Conductor conductor;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Asiento> asientos;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Viaje> viajes;
}
