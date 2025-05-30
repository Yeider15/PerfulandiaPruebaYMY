package com.ymy.cl.perfulandiafinal.YeiderVentas.service;

import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.DetalleVentaQueryDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.DetalleVenta;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.VentaRequestDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.DetalleVentaRequestDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.VentaDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.repository.VentaRepository;
import com.ymy.cl.perfulandiafinal.YeiderVentas.repository.DetalleVentaRepository;
import com.ymy.cl.perfulandiafinal.MariaUsuario.service.UsuarioService;
import com.ymy.cl.perfulandiafinal.MariaUsuario.model.Usuario;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    // Metodo para crear una venta
    @Transactional
    public String crearVenta(VentaRequestDTO ventaRequestDTO) {
        // Obtener el usuario que realizó la venta
        Usuario usuario = usuarioService.findById(ventaRequestDTO.getUsuarioId().intValue()); // Aseguramos que sea Integer

        // Crear la instancia de Venta
        Venta venta = new Venta();
        venta.setFechaVenta(new Date());
        venta.setEstado(ventaRequestDTO.getEstado());
        venta.setTotal(ventaRequestDTO.getTotal());
        venta.setUsuario(usuario);

        // Inicializar la lista de detalles de venta
        List<DetalleVenta> detalleVentas = new ArrayList<>();

        // Crear y guardar los detalles de la venta
        for (DetalleVentaRequestDTO detalleDTO : ventaRequestDTO.getDetallesVenta()) {
            DetalleVenta detalleVenta = new DetalleVenta();

            // Obtener el producto relacionado con el detalle
            Producto producto = productoService.findById(detalleDTO.getProductoId().intValue()); // Aseguramos que sea Integer

            // Setear los detalles de la venta
            detalleVenta.setCantidad(detalleDTO.getCantidad());
            detalleVenta.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalleVenta.setProductoModel(producto);
            detalleVenta.setVenta(venta); // Asociar el detalle con la venta

            // Añadir el detalle a la lista
            detalleVentas.add(detalleVenta);

            // Actualizar el stock del producto
            productoService.updateStock(producto.getId(), detalleDTO.getCantidad());
        }

        // Asignar la lista de detalles a la venta
        venta.setDetalleVentas(detalleVentas);

        // Guardar la venta con los detalles asociados
        ventaRepository.save(venta);  // Esto debería guardar correctamente la venta y los detalles

        return "Venta creada con éxito";
    }



    // Metodo para obtener todas las ventas
    public List<VentaDTO> findAllVentas() {
        List<VentaDTO> ventasDTO = new ArrayList<>();
        List<Venta> ventas = ventaRepository.findAll(); // Obtener todas las ventas

        for (Venta venta : ventas) {
            ventasDTO.add(new VentaDTO(venta)); // Convertir cada venta a DTO
        }

        return ventasDTO;
    }

    // Metodo para obtener una venta por su ID
    public VentaDTO findVentaById(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        return new VentaDTO(venta); // Convertir la entidad Venta a DTO
    }

    // Metodo para actualizar el estado de una venta
    public String actualizarEstado(Long id, String estado) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        venta.setEstado(estado);
        ventaRepository.save(venta); // Guardar la venta con el nuevo estado
        return "Estado de la venta actualizado con éxito";
    }

    // Metodo para obtener ventas por estado
    public List<VentaDTO> findVentasByEstado(String estado) {
        List<Venta> ventas = ventaRepository.findByEstado(estado); // Buscar ventas por estado
        List<VentaDTO> ventasDTO = new ArrayList<>();

        for (Venta venta : ventas) {
            ventasDTO.add(new VentaDTO(venta)); // Convertir cada venta a DTO
        }

        return ventasDTO;
    }

    // Metodo para eliminar una venta por su ID
    public String eliminarVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        ventaRepository.delete(venta);  // Eliminar la venta de la base de datos
        return "Venta eliminada con éxito";
    }


}
