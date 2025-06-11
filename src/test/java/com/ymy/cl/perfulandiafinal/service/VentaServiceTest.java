package com.ymy.cl.perfulandiafinal.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.VentaRequestDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.DetalleVentaRequestDTO; // Import this
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.VentaDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import com.ymy.cl.perfulandiafinal.YeiderVentas.repository.VentaRepository;
import com.ymy.cl.perfulandiafinal.YeiderVentas.repository.DetalleVentaRepository;
import com.ymy.cl.perfulandiafinal.MariaUsuario.service.UsuarioService;
import com.ymy.cl.perfulandiafinal.MariaUsuario.model.Usuario;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;

import com.ymy.cl.perfulandiafinal.YeiderVentas.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@SpringBootTest
public class VentaServiceTest {

    @Autowired
    private VentaService ventaService;

    @MockBean
    private VentaRepository ventaRepository;

    @MockBean
    private DetalleVentaRepository detalleVentaRepository;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ProductoService productoService;

    @Test
    public void testCrearVenta() {
        Long usuarioId = 1L; // Keep usuarioId as Long for VentaRequestDTO
        Integer productoId = 1; // Producto ID is Integer

        VentaRequestDTO ventaRequestDTO = new VentaRequestDTO();
        ventaRequestDTO.setUsuarioId(usuarioId); // Set as Long, as expected by VentaRequestDTO
        ventaRequestDTO.setEstado("PENDIENTE");
        ventaRequestDTO.setTotal(200.0);

        // Create a list of DetalleVentaRequestDTO
        List<DetalleVentaRequestDTO> detallesVenta = new ArrayList<>();
        DetalleVentaRequestDTO detalle1 = new DetalleVentaRequestDTO();
        detalle1.setProductoId(productoId.longValue()); // Convert Integer product ID to Long for DetalleVentaRequestDTO
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(100.0);
        detallesVenta.add(detalle1);
        ventaRequestDTO.setDetallesVenta(detallesVenta); // Set the list of details

        // Usuario y Producto simulados
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId.intValue()); // Usuario model expects Integer ID
        usuario.setNombre("Juan Pérez");

        Producto producto = new Producto();
        producto.setId(productoId); // Producto model expects Integer ID
        producto.setNombre("Producto A");
        producto.setStock(10); // Set an initial stock for updateStock to work

        // Simulamos las llamadas a los servicios
        // Mock the findById calls for both services, ensuring the correct ID types are used for the service methods
        when(usuarioService.findById(usuarioId.intValue())).thenReturn(usuario); // Service expects Integer
        when(productoService.findById(productoId)).thenReturn(producto); // Service expects Integer

        // Mock the updateStock call
        when(productoService.updateStock(any(Integer.class), any(Integer.class))).thenReturn("Stock actualizado con éxito para el producto: Producto A");

        // Simulamos la venta
        Venta venta = new Venta();
        venta.setId(1L); // Assign an ID to the Venta object
        venta.setFechaVenta(new Date());
        venta.setEstado(ventaRequestDTO.getEstado());
        venta.setTotal(ventaRequestDTO.getTotal());
        venta.setUsuario(usuario);

        // Simulate saving the Venta
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        // Call the service method
        String mensaje = ventaService.crearVenta(ventaRequestDTO);

        // Assertions and verifications
        assertEquals("Venta creada con éxito", mensaje);
        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(productoService, times(1)).updateStock(productoId, detalle1.getCantidad()); // Verify updateStock is called
    }

    @Test
    public void testFindAllVentas() {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setFechaVenta(new Date());
        venta.setEstado("PENDIENTE");
        venta.setTotal(200.0);

        Usuario usuario = new Usuario(); // A Venta has a Usuario, so set one
        usuario.setId(1);
        usuario.setNombre("Test User");
        venta.setUsuario(usuario);

        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<VentaDTO> ventas = ventaService.findAllVentas();
        assertNotNull(ventas);
        assertEquals(1, ventas.size());
        assertEquals("PENDIENTE", ventas.get(0).getEstado());
        assertEquals("Test User", ventas.get(0).getUsuarioNombre()); // Check user name
    }

    @Test
    public void testFindVentaById() {
        Long id = 1L;
        Venta venta = new Venta();
        venta.setId(id);
        venta.setFechaVenta(new Date());
        venta.setEstado("PENDIENTE");
        venta.setTotal(200.0);

        Usuario usuario = new Usuario(); // A Venta has a Usuario, so set one
        usuario.setId(1);
        usuario.setNombre("Test User");
        venta.setUsuario(usuario);

        when(ventaRepository.findById(id)).thenReturn(java.util.Optional.of(venta));

        VentaDTO foundVenta = ventaService.findVentaById(id);

        assertNotNull(foundVenta);
        assertEquals(id, foundVenta.getId());
        assertEquals("Test User", foundVenta.getUsuarioNombre()); // Check user name
    }

    @Test
    public void testActualizarEstado() {
        Long id = 1L;
        String nuevoEstado = "FINALIZADO";
        Venta venta = new Venta();
        venta.setId(id);
        venta.setEstado("PENDIENTE");

        when(ventaRepository.findById(id)).thenReturn(java.util.Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta); // Mock save call

        String mensaje = ventaService.actualizarEstado(id, nuevoEstado);

        assertEquals("Estado de la venta actualizado con éxito", mensaje);
        assertEquals(nuevoEstado, venta.getEstado());
        verify(ventaRepository, times(1)).save(venta); // Verify save is called
    }

    @Test
    public void testFindVentasByEstado() {
        String estado = "PENDIENTE";
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setEstado(estado);

        Usuario usuario = new Usuario(); // A Venta has a Usuario, so set one
        usuario.setId(1);
        usuario.setNombre("Test User");
        venta.setUsuario(usuario);

        when(ventaRepository.findByEstado(estado)).thenReturn(List.of(venta));

        List<VentaDTO> ventas = ventaService.findVentasByEstado(estado);

        assertNotNull(ventas);
        assertEquals(1, ventas.size());
        assertEquals(estado, ventas.get(0).getEstado());
        assertEquals("Test User", ventas.get(0).getUsuarioNombre()); // Check user name
    }

    @Test
    public void testEliminarVenta() {
        Long id = 1L;
        Venta venta = new Venta();
        venta.setId(id);

        when(ventaRepository.findById(id)).thenReturn(java.util.Optional.of(venta));
        doNothing().when(ventaRepository).delete(any(Venta.class)); // Mock void method

        String mensaje = ventaService.eliminarVenta(id);

        assertEquals("Venta eliminada con éxito", mensaje);
        verify(ventaRepository, times(1)).delete(venta);
    }
}