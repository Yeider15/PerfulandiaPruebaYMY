package com.ymy.cl.perfulandiafinal.YeiderVentas.controller;

import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.request.VentaRequestDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.VentaDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.dto.response.DetalleVentaQueryDTO;
import com.ymy.cl.perfulandiafinal.YeiderVentas.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<String> crearVenta(@RequestBody VentaRequestDTO ventaRequestDTO) {
        try {
            String mensaje = ventaService.crearVenta(ventaRequestDTO);  // Llamar al servicio para crear la venta
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());  // 500 Internal Server Error
        }
    }

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<VentaDTO>> listarVentas() {
        List<VentaDTO> ventas = ventaService.findAllVentas();  // Llamamos al servicio para obtener todas las ventas
        if (ventas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ventas);  // 204 No Content
        }
        return ResponseEntity.ok(ventas);  // 200 OK
    }

    // Obtener una venta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVentaPorId(@PathVariable Long id) {
        try {
            VentaDTO venta = ventaService.findVentaById(id);  // Llamamos al servicio para obtener la venta
            return ResponseEntity.ok(venta);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found
        }
    }

    // Actualizar el estado de una venta
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> estadoMap) {
        try {
            String estado = estadoMap.get("estado");  // Obtener el estado del cuerpo de la solicitud
            String mensaje = ventaService.actualizarEstado(id, estado);  // Llamamos al servicio para actualizar el estado
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada");  // 404 Not Found
        }
    }

    // Obtener ventas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VentaDTO>> listarVentasPorEstado(@PathVariable String estado) {
        List<VentaDTO> ventas = ventaService.findVentasByEstado(estado);  // Llamamos al servicio para obtener las ventas por estado
        if (ventas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ventas);  // 204 No Content
        }
        return ResponseEntity.ok(ventas);  // 200 OK
    }

    // Eliminar una venta por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVenta(@PathVariable Long id) {
        try {
            String mensaje = ventaService.eliminarVenta(id);  // Llamamos al servicio para eliminar la venta
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada");  // 404 Not Found
        }
    }



}
