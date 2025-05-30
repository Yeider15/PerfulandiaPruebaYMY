package com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request;

import lombok.Data;

@Data
public class DetalleVentaRequestDTO {
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
}
