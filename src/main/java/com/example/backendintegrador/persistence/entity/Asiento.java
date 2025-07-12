package com.example.backendintegrador.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "asiento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento")
    private Integer idAsiento;

    @NotNull(message = "El piso es obligatorio")
    @Min(value = 1, message = "El piso mínimo es 1")
    @Max(value = 2, message = "El piso máximo es 2")
    @Column(name = "piso", nullable = false)
    private Integer piso;

    @NotBlank(message = "La columna es obligatorio")
    @Column(name = "columna", nullable = false)
    private String columna;

    @NotBlank(message = "La fila es obligatorio")
    @Column(name = "fila", nullable = false)
    private String fila;

    @Column(name = "descripcion")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false)
    private String estado; // disponible, ocupado, reservado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bus")
    @JsonBackReference // Bus es el lado "padre"
    private Bus bus;

    @OneToMany(mappedBy = "asiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // UsuarioAsientos son el lado "hijo"
    private Set<UsuarioAsiento> usuarioAsientos;
}
