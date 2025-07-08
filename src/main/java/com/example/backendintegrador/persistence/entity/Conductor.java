package com.example.backendintegrador.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "conductor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajador")
    private Integer idTrabajador;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @NotBlank(message = "El número de licencia es obligatorio")
    @Column(name = "numero_licencia_conduccion", nullable = false, unique = true)
    private String numeroLicenciaConduccion;

    @OneToMany(mappedBy = "conductor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bus> buses;
}
