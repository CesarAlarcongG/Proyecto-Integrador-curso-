package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Asiento;
import com.example.backendintegrador.persistence.entity.Bus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {
    Optional<Bus> findByPlaca(String placa);

    boolean existsByPlaca(@NotBlank(message = "La placa es obligatoria") String placa);

}