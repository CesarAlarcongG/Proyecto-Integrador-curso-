package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Pasaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PasajeRepository extends JpaRepository<Pasaje, Integer> {
    List<Pasaje> findByUsuario_IdUsuario(Integer idUsuario);
}

