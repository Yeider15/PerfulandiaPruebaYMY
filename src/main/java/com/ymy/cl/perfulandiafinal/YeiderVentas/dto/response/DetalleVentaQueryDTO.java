package com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response;

public interface DetalleVentaQueryDTO {
    Long getId();
    Integer getCantidad();
    Double getPrecioUnitario();
    String getNombreProducto();  // Suponiendo que deseas obtener el nombre del producto
}
