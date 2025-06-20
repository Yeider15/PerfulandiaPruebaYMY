package com.ymy.cl.perfulandiafinal.Inventario.YaquelinController;

import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinService.ProductoService;
import com.ymy.cl.perfulandiafinal.assembler.ProductoModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/inventario")
public class ProductoControllerV2 {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    // GET: listar todos los productos
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withSelfRel());
    }

    // GET: obtener producto por id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        Producto producto = productoService.findById(id);
        return assembler.toModel(producto);
    }

    // POST: crear producto
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto) {
        Producto nuevo = productoService.saveAndReturn(producto); // Necesita este método en el service
        return ResponseEntity
                .created(linkTo(methodOn(ProductoControllerV2.class).obtenerProductoPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    // DELETE: eliminar producto por id
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PUT: actualizar stock
    @PutMapping(value = "/{id}/stock", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> actualizarStock(@PathVariable Integer id, @RequestParam int cantidadVendida) {
        productoService.updateStock(id, cantidadVendida);
        Producto actualizado = productoService.findById(id);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }



}
