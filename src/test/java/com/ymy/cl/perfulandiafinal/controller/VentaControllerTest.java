package com.ymy.cl.perfulandiafinal.controller; // Mantenemos el paquete original

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymy.cl.perfulandiafinal.YeiderVentas.controller.VentaController; // Necesario para @WebMvcTest
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.VentaRequestDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.DetalleVentaRequestDTO; // Necesario para VentaRequestDTO
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.VentaDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.service.VentaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap; // Para crear Map para el cuerpo de la solicitud
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de prueba para VentaController.
 * Utiliza @WebMvcTest para enfocar la prueba en la capa web,
 * y @MockBean para simular el comportamiento de VentaService.
 * Se adapta a la estructura existente del VentaController sin modificarlo.
 */
@WebMvcTest(VentaController.class)
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para realizar solicitudes HTTP simuladas

    @MockBean
    private VentaService ventaService; // Simula el servicio de ventas

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos Java a JSON y viceversa

    private VentaDTO ventaDTO; // Objeto DTO de respuesta común para los tests
    private VentaRequestDTO ventaRequestDTO; // Objeto DTO de solicitud común para los tests

    // Definición de la ruta base de la API del controlador
    private static final String BASE_API_PATH = "/api/v1/ventas";

    /**
     * Configuración inicial antes de cada test.
     * Inicializa los DTOs de prueba.
     */
    @BeforeEach
    void setUp() {
        // Inicializa un VentaDTO para las respuestas simuladas
        ventaDTO = new VentaDTO();
        ventaDTO.setId(1L);
        ventaDTO.setFechaVenta(new Date());
        ventaDTO.setEstado("PENDIENTE");
        ventaDTO.setTotal(150.0);
        // CORRECCIÓN: Usar setUsuarioNombre en lugar de setUsuarioId, ya que VentaDTO tiene usuarioNombre
        ventaDTO.setUsuarioNombre("Juan Test");


        // Inicializa un VentaRequestDTO para las solicitudes simuladas
        ventaRequestDTO = new VentaRequestDTO();
        ventaRequestDTO.setUsuarioId(1L); // VentaRequestDTO sí tiene usuarioId (Long)
        ventaRequestDTO.setEstado("PENDIENTE");
        ventaRequestDTO.setTotal(200.0);
        List<DetalleVentaRequestDTO> detalles = new ArrayList<>();
        DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
        detalle.setProductoId(101L);
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(200.0);
        detalles.add(detalle);
        ventaRequestDTO.setDetallesVenta(detalles);
    }

    /**
     * Prueba la creación de una nueva venta a través del controlador.
     * Simula una solicitud POST a /api/v1/ventas.
     * Espera un status 201 Created.
     */
    @Test
    void testCrearVenta() throws Exception {
        // Configura el mock del servicio para devolver un mensaje de éxito
        when(ventaService.crearVenta(any(VentaRequestDTO.class))).thenReturn("Venta creada con éxito");

        // Realiza la solicitud POST y verifica la respuesta
        mockMvc.perform(post(BASE_API_PATH) // Usa la ruta base
                        .contentType(MediaType.APPLICATION_JSON) // Establece el tipo de contenido a JSON
                        .content(objectMapper.writeValueAsString(ventaRequestDTO))) // Convierte el DTO a JSON
                .andExpect(status().isCreated()) // ¡Esperamos un status 201 CREATED!
                .andExpect(content().string("Venta creada con éxito")); // Espera el mensaje de éxito
    }

    /**
     * Prueba la obtención de todas las ventas.
     * Simula una solicitud GET a /api/v1/ventas.
     * Espera un status 200 OK.
     */
    @Test
    void testListarVentas() throws Exception {
        // Configura el mock del servicio para devolver una lista de VentaDTO
        when(ventaService.findAllVentas()).thenReturn(Collections.singletonList(ventaDTO));

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get(BASE_API_PATH) // Usa la ruta base
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera un status 200 OK
                .andExpect(jsonPath("$[0].id").value(ventaDTO.getId())) // Verifica el ID del primer elemento en la lista
                .andExpect(jsonPath("$[0].estado").value(ventaDTO.getEstado())); // Verifica el estado del primer elemento
    }

    /**
     * Prueba la obtención de todas las ventas cuando no hay contenido.
     * Simula una solicitud GET a /api/v1/ventas.
     * Espera un status 204 No Content.
     */
    @Test
    void testListarVentas_NoContent() throws Exception {
        // Configura el mock del servicio para devolver una lista vacía
        when(ventaService.findAllVentas()).thenReturn(Collections.emptyList());

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get(BASE_API_PATH) // Usa la ruta base
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // ¡Esperamos un status 204 NO_CONTENT!
    }

    /**
     * Prueba la obtención de una venta por su ID.
     * Simula una solicitud GET a /api/v1/ventas/{id}.
     * Espera un status 200 OK.
     */
    @Test
    void testObtenerVentaPorId() throws Exception {
        Long ventaId = 1L;
        // Configura el mock del servicio para devolver un VentaDTO específico
        when(ventaService.findVentaById(ventaId)).thenReturn(ventaDTO);

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get(BASE_API_PATH + "/{id}", ventaId) // Usa la ruta base + ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera un status 200 OK
                .andExpect(jsonPath("$.id").value(ventaDTO.getId())) // Verifica el ID en el objeto JSON
                .andExpect(jsonPath("$.estado").value(ventaDTO.getEstado())); // Verifica el estado en el objeto JSON
    }

    /**
     * Prueba la obtención de una venta por su ID cuando no existe.
     * Simula una solicitud GET a /api/v1/ventas/{id} y espera un 404.
     */
    @Test
    void testObtenerVentaPorId_NotFound() throws Exception {
        Long nonExistentId = 99L;
        // Configura el mock del servicio para lanzar una RuntimeException
        when(ventaService.findVentaById(nonExistentId)).thenThrow(new RuntimeException("Venta no encontrada"));

        // Realiza la solicitud GET y espera un status 404 Not Found
        mockMvc.perform(get(BASE_API_PATH + "/{id}", nonExistentId) // Usa la ruta base + ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Espera un status 404 NOT_FOUND
    }

    /**
     * Prueba la actualización del estado de una venta.
     * Simula una solicitud PUT a /api/v1/ventas/{id}/estado.
     * Espera un status 200 OK.
     */
    @Test
    void testActualizarEstado() throws Exception {
        Long ventaId = 1L;
        String nuevoEstado = "FINALIZADO";

        // Prepara el cuerpo de la solicitud como un Map<String, String> para simular el RequestBody
        Map<String, String> estadoMap = new HashMap<>();
        estadoMap.put("estado", nuevoEstado);

        // Configura el mock del servicio para devolver un mensaje de éxito
        when(ventaService.actualizarEstado(eq(ventaId), eq(nuevoEstado))).thenReturn("Estado de la venta actualizado con éxito");

        // Realiza la solicitud PUT y verifica la respuesta
        mockMvc.perform(put(BASE_API_PATH + "/{id}/estado", ventaId) // Usa PUT y la ruta base + ID + /estado
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoMap))) // Envía el Map como JSON
                .andExpect(status().isOk()) // Espera un status 200 OK
                .andExpect(content().string("Estado de la venta actualizado con éxito")); // Espera el mensaje de éxito
    }

    /**
     * Prueba la actualización del estado de una venta cuando la venta no existe.
     * Simula una solicitud PUT a /api/v1/ventas/{id}/estado.
     * Espera un status 404 Not Found.
     */
    @Test
    void testActualizarEstado_NotFound() throws Exception {
        Long nonExistentId = 99L;
        String nuevoEstado = "FINALIZADO";

        // Prepara el cuerpo de la solicitud
        Map<String, String> estadoMap = new HashMap<>();
        estadoMap.put("estado", nuevoEstado);

        // Configura el mock del servicio para lanzar una RuntimeException
        when(ventaService.actualizarEstado(eq(nonExistentId), eq(nuevoEstado))).thenThrow(new RuntimeException("Venta no encontrada"));

        // Realiza la solicitud PUT y verifica la respuesta
        mockMvc.perform(put(BASE_API_PATH + "/{id}/estado", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoMap)))
                .andExpect(status().isNotFound()) // Espera un status 404 NOT_FOUND
                .andExpect(content().string("Venta no encontrada")); // Verifica el mensaje de error
    }

    /**
     * Prueba la obtención de ventas por un estado específico.
     * Simula una solicitud GET a /api/v1/ventas/estado/{estado}.
     * Espera un status 200 OK.
     */
    @Test
    void testListarVentasPorEstado() throws Exception {
        String estado = "PENDIENTE";
        // Configura el mock del servicio para devolver una lista de VentaDTO
        when(ventaService.findVentasByEstado(estado)).thenReturn(Collections.singletonList(ventaDTO));

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get(BASE_API_PATH + "/estado/{estado}", estado) // Usa la ruta base + /estado + estado
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera un status 200 OK
                .andExpect(jsonPath("$[0].estado").value(ventaDTO.getEstado())); // Verifica el estado del primer elemento
    }

    /**
     * Prueba la obtención de ventas por estado cuando no hay contenido.
     * Simula una solicitud GET a /api/v1/ventas/estado/{estado}.
     * Espera un status 204 No Content.
     */
    @Test
    void testListarVentasPorEstado_NoContent() throws Exception {
        String estado = "INEXISTENTE";
        // Configura el mock del servicio para devolver una lista vacía
        when(ventaService.findVentasByEstado(estado)).thenReturn(Collections.emptyList());

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get(BASE_API_PATH + "/estado/{estado}", estado)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Espera un status 204 NO_CONTENT
    }

    /**
     * Prueba la eliminación de una venta por su ID.
     * Simula una solicitud DELETE a /api/v1/ventas/{id}.
     * Espera un status 200 OK.
     */
    @Test
    void testEliminarVenta() throws Exception {
        Long ventaId = 1L;
        // Configura el mock del servicio para devolver un mensaje de éxito
        when(ventaService.eliminarVenta(ventaId)).thenReturn("Venta eliminada con éxito");

        // Realiza la solicitud DELETE y verifica la respuesta
        mockMvc.perform(delete(BASE_API_PATH + "/{id}", ventaId) // Usa la ruta base + ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera un status 200 OK
                .andExpect(content().string("Venta eliminada con éxito")); // Espera el mensaje de éxito
    }

    /**
     * Prueba la eliminación de una venta por su ID cuando no existe.
     * Simula una solicitud DELETE a /api/v1/ventas/{id}.
     * Espera un status 404 Not Found.
     */
    @Test
    void testEliminarVenta_NotFound() throws Exception {
        Long nonExistentId = 99L;
        // Configura el mock del servicio para lanzar una RuntimeException
        when(ventaService.eliminarVenta(nonExistentId)).thenThrow(new RuntimeException("Venta no encontrada"));

        // Realiza la solicitud DELETE y verifica la respuesta
        mockMvc.perform(delete(BASE_API_PATH + "/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Espera un status 404 NOT_FOUND
                .andExpect(content().string("Venta no encontrada")); // Verifica el mensaje de error
    }
}
