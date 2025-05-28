package com.ymy.cl.perfulandiafinal.Inventario.YaquelinController;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productos);  // 204 No Content
        }
        return ResponseEntity.ok(productos);  // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        try {
            Producto producto = productoService.findById(id);
            return ResponseEntity.ok(producto);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found
        }
    }

    @PostMapping
    public ResponseEntity<String> crearProducto(@RequestBody Producto producto) {
        try {
            String mensaje = productoService.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());  // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
        try {
            String mensaje = productoService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");  // 404 Not Found
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<String> actualizarStock(@PathVariable Integer id, @RequestParam int cantidadVendida) {
        try {
            String mensaje = productoService.updateStock(id, cantidadVendida);
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  // 400 Bad Request
        }
    }

}
