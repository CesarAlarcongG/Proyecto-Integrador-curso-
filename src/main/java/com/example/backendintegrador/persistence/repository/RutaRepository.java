package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
}