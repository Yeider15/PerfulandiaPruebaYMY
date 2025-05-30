package com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response;

import com.ymy.cl.perfulandiafinal.YeiderVentas.model.DetalleVenta;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class VentaDTO {

    private Long id;
    private Date fechaVenta;
    private Double total;
    private String estado;
    private String usuarioNombre;
    private List<DetalleVentaDTO> detalleVentas = new ArrayList<>();

    public VentaDTO(Venta venta) {
        this.id = venta.getId();
        this.fechaVenta = venta.getFechaVenta();
        this.total = venta.getTotal();
        this.estado = venta.getEstado();
        this.usuarioNombre = venta.getUsuario().getNombre();

        // Convertir los detalles de venta a DTOs
        if (venta.getDetalleVentas() != null) {
            for (DetalleVenta detalle : venta.getDetalleVentas()) {
                this.detalleVentas.add(new DetalleVentaDTO(detalle));  // Convertir cada detalle de venta a DTO
            }
        }
    }
}
