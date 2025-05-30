package com.ymy.cl.perfulandiafinal.YeiderVentas.repository;

import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.DetalleVentaQueryDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.VentaQueryDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Consulta personalizada para obtener ventas con el nombre del usuario
    @Query("SELECT v.id AS id, v.total AS total, v.estado AS estado, u.nombre AS usuarioNombre " +
            "FROM Venta v JOIN v.usuario u WHERE v.estado = :estado")
    List<VentaQueryDTO> findVentasByEstado(@Param("estado") String estado);

    // Mwtodo para obtener los detalles de la venta por ID
    @Query("SELECT d.id AS id, d.cantidad AS cantidad, d.precioUnitario AS precioUnitario, p.nombre AS nombreProducto " +
            "FROM DetalleVenta d JOIN d.productoModel p WHERE d.venta.id = :ventaId")
    List<DetalleVentaQueryDTO> findDetallesDeVenta(@Param("ventaId") Long ventaId);

    List<Venta> findByEstado(String estado);
}
