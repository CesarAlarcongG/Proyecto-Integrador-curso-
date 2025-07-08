package com.example.backendintegrador.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario_asiento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioAsiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_asiento")
    private Integer idUsuarioAsiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asiento", nullable = false)
    private Asiento asiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasaje", nullable = false)
    private Pasaje pasaje;
}