package com.ymy.cl.perfulandiafinal.Inventario.repository;
import com.ymy.cl.perfulandiafinal.Inventario.model.PerfumeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfumeRepository extends JpaRepository<PerfumeModel, Integer> {
    // Buscar perfume por su nombre
    PerfumeModel findByNombre(String nombre);
}