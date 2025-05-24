package com.ymy.cl.perfulandiafinal.YeiderVentas.repository;


import com.ymy.cl.perfulandiafinal.YeiderVentas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    // Obtener detalles de venta por id de venta
    List<DetalleVenta> findByVentaId(Long ventaId);
}