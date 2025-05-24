package com.ymy.cl.perfulandiafinal.YeiderVentas.repository;


import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    // Buscar ventas por estado
    List<Venta> findByEstado(String estado);
}
