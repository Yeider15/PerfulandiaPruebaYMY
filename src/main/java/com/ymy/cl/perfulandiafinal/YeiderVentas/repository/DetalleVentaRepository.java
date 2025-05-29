package com.ymy.cl.perfulandiafinal.YeiderVentas.repository;

import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.DetalleVentaQueryDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    // Obtener detalles de venta por id de venta con proyección
    @Query("SELECT d.id AS id, d.cantidad AS cantidad, d.precioUnitario AS precioUnitario, p.nombre AS nombreProducto " +
            "FROM DetalleVenta d JOIN d.productoModel p WHERE d.venta.id = :ventaId")
    List<DetalleVentaQueryDTO> findDetalleVentaByVentaId(@Param("ventaId") Long ventaId);
}
