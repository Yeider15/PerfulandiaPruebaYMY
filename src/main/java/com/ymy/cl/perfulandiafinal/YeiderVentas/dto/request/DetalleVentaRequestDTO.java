package com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request;

import lombok.Data;

@Data
public class DetalleVentaRequestDTO {
    private Long productoId; // ID del producto
    private Integer cantidad; // Cantidad de productos vendidos
    private Double precioUnitario; // Precio unitario del producto
}
