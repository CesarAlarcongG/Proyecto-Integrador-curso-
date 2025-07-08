package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Conductor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Integer> {
    Optional<Conductor> findByDni(String dni);
    Optional<Conductor> findByNumeroLicenciaConduccion(String numeroLicencia);

    boolean existsByDni(@NotBlank(message = "El DNI es obligatorio") @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres") String dni);

    boolean existsByNumeroLicenciaConduccion(@NotBlank(message = "El número de licencia es obligatorio") String numeroLicenciaConduccion);
}