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
@Table(name = "pasaje")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pasaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasaje")
    private Integer idPasaje;

    @Positive(message = "El total a pagar debe ser mayor que cero")
    @Column(name = "total_pagar", nullable = false)
    private Double totalPagar;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference // Usuario es el lado "padre"
    private Usuario usuario;

    @NotNull(message = "La ruta es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", nullable = false)
    @JsonBackReference // Ruta es el lado "padre"
    private Ruta ruta;

    @NotNull(message = "El viaje es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viaje", nullable = false)
    @JsonBackReference // Viaje es el lado "padre"
    private Viaje viaje;

    @OneToMany(mappedBy = "pasaje", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // UsuarioAsientos son el lado "hijo"
    private Set<UsuarioAsiento> usuarioAsientos;
}