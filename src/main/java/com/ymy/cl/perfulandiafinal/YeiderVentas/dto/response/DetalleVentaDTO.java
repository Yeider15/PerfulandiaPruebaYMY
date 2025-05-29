package com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response;

import com.ymy.cl.perfulandiafinal.YeiderVentas.model.DetalleVenta;
import lombok.Data;

@Data
public class DetalleVentaDTO {
    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
    private String nombreProducto;

    // Constructor para convertir una entidad DetalleVenta a DetalleVentaDTO
    public DetalleVentaDTO(DetalleVenta detalleVenta) {
        this.id = detalleVenta.getId();
        this.cantidad = detalleVenta.getCantidad();
        this.precioUnitario = detalleVenta.getPrecioUnitario();

        // Obtener el nombre del producto asociado al detalle
        if (detalleVenta.getProductoModel() != null) {
            this.nombreProducto = detalleVenta.getProductoModel().getNombre(); // Suponiendo que `Producto` tiene un método `getNombre()`
        }
    }
}
