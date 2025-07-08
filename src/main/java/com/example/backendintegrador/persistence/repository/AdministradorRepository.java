package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Administrador;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByCorreo(String correo);

    boolean existsByCorreo(@NotBlank(message = "El correo es obligatorio") @Email(message = "El correo debe ser válido") String correo);
}