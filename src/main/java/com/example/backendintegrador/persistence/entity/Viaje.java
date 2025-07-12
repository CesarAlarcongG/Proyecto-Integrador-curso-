package com.example.backendintegrador.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "viaje")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutas")
    private Integer idRutas;

    @NotNull(message = "La hora de salida es obligatoria")
    @Column(name = "hora_salida", nullable = false)
    private LocalTime horaSalida;

    @NotNull(message = "La fecha de salida es obligatoria")
    @FutureOrPresent(message = "La fecha de salida debe ser hoy o en el futuro")
    @Column(name = "fecha_salida", nullable = false)
    private LocalDate fechaSalida;

    @Positive(message = "El costo debe ser mayor que cero")
    @Column(name = "costo", nullable = false)
    private Double costo;

    @NotNull(message = "La ruta es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", nullable = false)
    @JsonBackReference // Ruta es el lado "padre"
    private Ruta ruta;

    @NotNull(message = "El bus es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carro", nullable = false)
    @JsonBackReference // Bus es el lado "padre"
    private Bus bus;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Pasajes son el lado "hijo"
    private Set<Pasaje> pasajes;

    private String estado;
}
