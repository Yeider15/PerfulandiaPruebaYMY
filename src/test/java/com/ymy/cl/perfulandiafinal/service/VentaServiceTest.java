package com.ymy.cl.perfulandiafinal.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.VentaRequestDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.DetalleVentaRequestDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.VentaDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import com.ymy.cl.perfulandiafinal.YeiderVentas.repository.VentaRepository;
import com.ymy.cl.perfulandiafinal.MariaUsuario.service.UsuarioService;
import com.ymy.cl.perfulandiafinal.MariaUsuario.model.Usuario;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.YeiderVentas.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class VentaServiceTest {

    @Autowired
    private VentaService ventaService;

    @MockBean
    private VentaRepository ventaRepository;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ProductoService productoService;

    @Test
    public void testCrearVenta() {
        // Preparar los datos
        Integer usuarioId = 1;  // Usamos Integer para usuarioId
        Integer productoId = 1; // Usamos Integer para productoId
        int cantidad = 2;
        double precioUnitario = 50.0;

        VentaRequestDTO ventaRequestDTO = new VentaRequestDTO();
        ventaRequestDTO.setUsuarioId(Long.valueOf(usuarioId));
        ventaRequestDTO.setEstado("ACTIVA");
        ventaRequestDTO.setTotal(100.0);

        DetalleVentaRequestDTO detalleDTO = new DetalleVentaRequestDTO();
        detalleDTO.setProductoId(Long.valueOf(productoId));
        detalleDTO.setCantidad(cantidad);
        detalleDTO.setPrecioUnitario(precioUnitario);

        List<DetalleVentaRequestDTO> detalles = new ArrayList<>();
        detalles.add(detalleDTO);
        ventaRequestDTO.setDetallesVenta(detalles);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        when(usuarioService.findById(usuarioId)).thenReturn(usuario);

        Producto producto = new Producto();
        producto.setId(productoId);
        when(productoService.findById(productoId)).thenReturn(producto);
        doNothing().when(productoService).updateStock(eq(productoId), anyInt());

        Venta venta = new Venta();
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        // Ejecutar el servicio
        String mensaje = ventaService.crearVenta(ventaRequestDTO);

        // Verificar resultados
        assertEquals("Venta creada con éxito", mensaje);
        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(productoService, times(1)).updateStock(eq(productoId), anyInt());
    }

    @Test
    public void testFindAllVentas() {

        Venta venta = new Venta();
        List<Venta> ventas = new ArrayList<>();
        ventas.add(venta);
        when(ventaRepository.findAll()).thenReturn(ventas);

        // Ejecutar el servicio
        List<VentaDTO> ventasDTO = ventaService.findAllVentas();

        // Verificar resultados
        assertNotNull(ventasDTO);
        assertEquals(1, ventasDTO.size());
    }

    @Test
    public void testFindVentaById() {
        Long id = 1L;
        Venta venta = new Venta();
        when(ventaRepository.findById(id)).thenReturn(java.util.Optional.of(venta));

        // Ejecutar el servicio
        VentaDTO ventaDTO = ventaService.findVentaById(id);

        // Verificar resultados
        assertNotNull(ventaDTO);
    }

    @Test
    public void testActualizarEstado() {
        Long id = 1L;
        String nuevoEstado = "COMPLETADA";
        Venta venta = new Venta();
        when(ventaRepository.findById(id)).thenReturn(java.util.Optional.of(venta));
        when(ventaRepository.save(venta)).thenReturn(venta);

        // Ejecutar el servicio
        String mensaje = ventaService.actualizarEstado(id, nuevoEstado);

        // Verificar resultados
        assertEquals("Estado de la venta actualizado con éxito", mensaje);
        assertEquals(nuevoEstado, venta.getEstado());
    }

    @Test
    public void testEliminarVenta() {
        Long id = 1L;
        Venta venta = new Venta();
        when(ventaRepository.findById(id)).thenReturn(java.util.Optional.of(venta));

        // Ejecutar el servicio
        String mensaje = ventaService.eliminarVenta(id);

        // Verificar resultados
        assertEquals("Venta eliminada con éxito", mensaje);
        verify(ventaRepository, times(1)).delete(venta);
    }
}
