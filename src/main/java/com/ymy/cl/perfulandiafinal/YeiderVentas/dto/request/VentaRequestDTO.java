package com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private Long id;
    private String estado;
    private Double total;
    private Long usuarioId;
    private List<DetalleVentaRequestDTO> detallesVenta; // Lista de detalles de venta
}
