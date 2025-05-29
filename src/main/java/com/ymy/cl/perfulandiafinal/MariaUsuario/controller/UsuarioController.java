package com.ymy.cl.perfulandiafinal.MariaUsuario.controller;

import com.ymy.cl.perfulandiafinal.MariaUsuario.model.Usuario;
import com.ymy.cl.perfulandiafinal.MariaUsuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(usuarios);  // 204 No Content
        }
        return ResponseEntity.ok(usuarios);  // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioService.findById(id);  // Llamamos al servicio para obtener el usuario por ID
            return ResponseEntity.ok(usuario);  // Retornamos el usuario con código 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Si no lo encuentra, 404 Not Found
        }
    }

    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        try {
            String mensaje = usuarioService.save(usuario);  // Llamamos al servicio para guardar el usuario
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());  //500 Internal Server Error
        }
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {
        try {
            String mensaje = usuarioService.delete(id);  // Llamamos al servicio para eliminar el usuario
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");  // 404 Not Found
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioActualizado) {
        try {
            usuarioActualizado.setId(id);
            String mensaje = usuarioService.save(usuarioActualizado);  // Usa el método save para actualizar
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");  // 404 Not Found
        }
    }



}

