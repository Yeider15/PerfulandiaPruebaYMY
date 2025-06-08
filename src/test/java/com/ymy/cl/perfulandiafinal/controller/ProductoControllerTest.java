package com.ymy.cl.perfulandiafinal.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinController.ProductoController;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Proporciona una manera de realizar peticiones HTTP en las pruebas

    @MockBean
    private ProductoService productoService; // Crea un mock del servicio de Producto

    @Autowired
    private ObjectMapper objectMapper; // Se usa para convertir objetos Java a JSON y viceversa

    private Producto producto;

    @BeforeEach
    void setUp() {
        // Configura un objeto Producto de ejemplo antes de cada prueba
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Producto A");
        producto.setPrecio(new BigDecimal(100.0));
        producto.setStock(10);
    }

    @Test
    public void testListarProductos() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con un Producto
        when(productoService.findAll()).thenReturn(List.of(producto));

        // Realiza una petición GET a /api/v1/inventario y verifica que la respuesta sea correcta
        mockMvc.perform(get("/api/v1/inventario"))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$[0].id").value(1)) // Verifica que el primer elemento tenga id 1
                .andExpect(jsonPath("$[0].nombre").value("Producto A")) // Verifica que el primer elemento tenga el nombre "Producto A"
                .andExpect(jsonPath("$[0].precio").value(100.0)); // Verifica que el primer elemento tenga el precio 100.0
    }

    @Test
    public void testObtenerProductoPorId() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findById() con 1, devuelve el objeto Producto
        when(productoService.findById(1)).thenReturn(producto);

        // Realiza una petición GET a /api/v1/inventario/1 y verifica que la respuesta sea correcta
        mockMvc.perform(get("/api/v1/inventario/1"))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$.id").value(1)) // Verifica que el id del objeto devuelto sea 1
                .andExpect(jsonPath("$.nombre").value("Producto A")) // Verifica que el nombre del objeto devuelto sea "Producto A"
                .andExpect(jsonPath("$.precio").value(100.0)); // Verifica que el precio del objeto devuelto sea 100.0
    }




    @Test
    public void testEliminarProducto() throws Exception {
        // Define el comportamiento del mock: cuando se llame a deleteById(), no hace nada
        when(productoService.delete(1)).thenReturn("Producto eliminado con éxito");

        // Realiza una petición DELETE a /api/estudiantes/1 y verifica que la respuesta sea correcta
        mockMvc.perform(delete("/api/v1/inventario/1"))
                .andExpect(status().isOk()); // Verifica que el estado de la respuesta sea 200 OK

        // Verifica que el método deleteById() del servicio se haya llamado exactamente una vez con el id 1
        verify(productoService, times(1)).delete(1);
    }


    @Test
    public void testActualizarStock() throws Exception {
        // Define el comportamiento del mock: cuando se llame a updateStock(), devuelve el mensaje "Stock actualizado"
        when(productoService.updateStock(1, 5)).thenReturn("Stock actualizado");

        // Realiza una petición PUT a /api/v1/inventario/1/stock con el parámetro cantidadVendida y verifica que la respuesta sea correcta
        mockMvc.perform(put("/api/v1/inventario/1/stock")
                        .param("cantidadVendida", "5"))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().string("Stock actualizado"));   // Verifica que el mensaje de la respuesta sea "Stock actualizado"
    }









}
