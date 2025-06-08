package com.ymy.cl.perfulandiafinal.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinRepository.ProductoRepository;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
@SpringBootTest
public class ProductoServiceTest {

    // Inyecta el servicio de Producto para ser probado.
    @Autowired
    private ProductoService productoService;

    // Crea un mock del repositorio de Producto para simular su comportamiento.
    @MockBean
    private ProductoRepository productoRepository;

    @Test
    public void testFindAll() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con un Producto.
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume B");

        // Se utiliza productoRepository en lugar de usuarioRepository
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        // Llama al método findAll() del servicio.
        List<Producto> productos = productoService.findAll();

        // Verifica que la lista devuelta no sea nula y que contenga exactamente un Producto.
        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Perfume B", productos.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Perfume B");

        // Define el comportamiento del mock: cuando se llame a findById(), devuelve un Producto.
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // Llama al método findById() del servicio.
        Producto foundProducto = productoService.findById(id);

        // Verifica que el Producto devuelto no sea nulo y que su nombre coincida con el esperado.
        assertNotNull(foundProducto);
        assertEquals(id, foundProducto.getId());
        assertEquals("Perfume B", foundProducto.getNombre());
    }

}
