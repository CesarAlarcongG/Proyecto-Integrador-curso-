package com.example.backendintegrador.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "usuario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 100, message = "La edad máxima es 100 años")
    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "permisos")
    private String permisos;

    @ManyToMany
    @JoinTable(
            name = "usuario_pasaje",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_pasaje")
    )
    @JsonManagedReference
    private Set<Pasaje> pasajes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // UsuarioAsientos son el lado "hijo"
    private Set<UsuarioAsiento> usuarioAsientos;
}
