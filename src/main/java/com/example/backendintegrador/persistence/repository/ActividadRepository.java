package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
}