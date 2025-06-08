package com.ymy.cl.perfulandiafinal.service;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinRepository.ProductoRepository;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class ProductoServiceTest {
    // Inyecta el servicio real a probar
    @Autowired
    private ProductoService productoService;

    // Crea un mock del repositorio para simular su comportamiento
    @MockBean
    private ProductoRepository productoRepository;

    @Test
    public void testFindAll() {
        // Crea un producto de prueba
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume A");

        // Simula el comportamiento del repositorio para devolver una lista con el producto
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        // Llama al método del servicio
        List<Producto> productos = productoService.findAll();

        // Verifica que la lista no sea nula y contenga exactamente un elemento con el nombre esperado
        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Perfume A", productos.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        // Crea un producto de prueba con ID
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume A");

        // Simula que el repositorio devuelve ese producto cuando se busca por ID
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // Llama al método del servicio
        Producto result = productoService.findById(1);

        // Verifica que el resultado no sea nulo y coincida el nombre
        assertNotNull(result);
        assertEquals("Perfume A", result.getNombre());
    }

    @Test
    public void testSave() {
        // Crea un producto a guardar
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume A");

        // Simula que el repositorio guarda el producto
        when(productoRepository.save(producto)).thenReturn(producto);

        // Llama al método del servicio
        String mensaje = productoService.save(producto);

        // Verifica que el mensaje sea el esperado
        assertEquals("Producto creado o actualizado con éxito", mensaje);
    }

    @Test
    public void testDelete() {
        // Simula que el repositorio no hace nada al eliminar
        doNothing().when(productoRepository).deleteById(1);

        // Llama al método del servicio para eliminar
        String mensaje = productoService.delete(1);

        // Verifica que se haya llamado exactamente una vez y el mensaje sea correcto
        verify(productoRepository, times(1)).deleteById(1);
        assertEquals("Producto eliminado con éxito", mensaje);
    }

    @Test
    public void testUpdateStock() {
        // Crea un producto con stock suficiente
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume A");
        producto.setStock(10);

        // Simula que se encuentra el producto por ID y que se guarda
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Llama al método de actualización de stock
        String mensaje = productoService.updateStock(1, 5);

        // Verifica que el mensaje sea correcto
        assertEquals("Stock actualizado con éxito para el producto: Perfume A", mensaje);
    }

    @Test
    public void testUpdateStockInsuficiente() {
        // Crea un producto con stock insuficiente
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume A");
        producto.setStock(3);

        // Simula que se encuentra el producto por ID
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // Verifica que se lance una excepción con el mensaje adecuado cuando no hay suficiente stock
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.updateStock(1, 5);
        });

        assertEquals("No hay suficiente stock de producto: Perfume A", exception.getMessage());
    }
}





