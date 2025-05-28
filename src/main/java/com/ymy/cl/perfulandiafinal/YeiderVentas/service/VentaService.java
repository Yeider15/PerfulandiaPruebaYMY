package com.ymy.cl.perfulandiafinal.YeiderVentas.service;

import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinRepository.ProductoRepository;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.DetalleVentaDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.DetalleVenta;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import com.ymy.cl.perfulandiafinal.YeiderVentas.repository.DetalleVentaRepository;
import com.ymy.cl.perfulandiafinal.YeiderVentas.repository.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public String crearVenta(Venta venta) {
        try {
            // Guardamos la venta
            Venta nuevaVenta = ventaRepository.save(venta);

            // Guardamos los detalles de la venta
            for (DetalleVenta detalle : venta.getDetalleVentas()) {
                detalle.setVenta(nuevaVenta);  // Asociamos el detalle a la venta

                // Guardamos el detalle
                detalleVentaRepository.save(detalle);

                // Actualizamos el stock
                actualizarStock(detalle.getProductoModel().getId(), detalle.getCantidad());
            }

            return "Venta registrada con éxito";
        } catch (Exception e) {
            // Registra el error y lanza una excepción controlada
            e.printStackTrace();
            throw new RuntimeException("Error al registrar la venta: " + e.getMessage());
        }
    }


    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    public List<DetalleVentaDTO> obtenerDetallesDeVentaID(Long ventaId) {
        // Obtener los detalles de la venta desde el repositorio
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);

        // Imprimir detalles para verificar que la entidad está siendo recuperada correctamente
        detalles.forEach(detalle -> System.out.println(detalle.getId())); // Agrega un log de depuración

        // Convertir las entidades DetalleVenta a DTO
        return detalles.stream()
                .map(DetalleVentaDTO::new)
                .collect(Collectors.toList());  // Recoger los DTOs en una lista
    }


    public String eliminarVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada");
        }
        ventaRepository.deleteById(id);
        return "Venta anulada con éxito";
    }

    // Actualizar el stock de un perfume después de la venta
    private void actualizarStock(Integer perfumeId, int cantidadVendida) {
        Producto productoModel = productoRepository.findById(perfumeId)
                .orElseThrow(() -> new RuntimeException("Perfume no encontrado"));
        int nuevoStock = productoModel.getStock() - cantidadVendida;

        if (nuevoStock < 0) {
            throw new RuntimeException("No hay suficiente stock para el perfume: " + productoModel.getNombre());
        }

        productoModel.setStock(nuevoStock);
        productoRepository.save(productoModel);  // Guardamos el perfume actualizado
    }

}
