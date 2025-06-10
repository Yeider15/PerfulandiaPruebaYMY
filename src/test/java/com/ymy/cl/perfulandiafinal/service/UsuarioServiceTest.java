package com.ymy.cl.perfulandiafinal.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ymy.cl.perfulandiafinal.MariaUsuario.model.Usuario;
import com.ymy.cl.perfulandiafinal.MariaUsuario.repository.UsuarioRepository;
import com.ymy.cl.perfulandiafinal.MariaUsuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindAll() {
        // Creamos un usuario para probar findAll
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario(1, "Juan Pérez", "Lopez", 123456789, "juan.perez@email.com", "Calle Falsa 123", new Date(), null)));

        // Llamamos al servicio para obtener todos los usuarios
        List<Usuario> usuarios = usuarioService.findAll();

        // Verificamos que la lista no esté vacía y contenga el usuario esperado
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("Juan Pérez", usuarios.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Usuario usuario = new Usuario(id, "Juan Pérez", "Lopez", 123456789, "juan.perez@email.com", "Calle Falsa 123", new Date(), null);

        // Simulamos el comportamiento de findById del repositorio
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Llamamos al servicio para obtener el usuario por ID
        Usuario found = usuarioService.findById(id);

        // Verificamos que el usuario encontrado no sea null y que el ID sea el esperado
        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Juan Pérez", found.getNombre());
    }

    @Test
    public void testSave() {
        // Creamos un usuario para prueba con todos los campos
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Juan Pérez");
        usuario.setApellido("Lopez");
        usuario.setTelefono(123456789);
        usuario.setCorreo("juan.perez@email.com");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setFechaRegistro(new Date()); // Aquí corregimos la fecha

        // Simulamos el comportamiento de save del repositorio
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // Llamamos al servicio para guardar el usuario y obtenemos el resultado (mensaje)
        String resultado = usuarioService.save(usuario);

        // Verificamos que el mensaje de éxito sea el esperado
        assertNotNull(resultado);
        assertEquals("Usuario creado o actualizado con éxito", resultado);

        // Verificamos que el método save haya sido llamado una vez
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        // Creamos un usuario para la prueba
        Usuario usuario = new Usuario(id, "Juan Pérez", "Lopez", 123456789, "juan.perez@email.com", "Calle Falsa 123", new Date(), null);

        // Simulamos el comportamiento de findById para devolver el usuario
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Simulamos el comportamiento de deleteById (no hace nada)
        doNothing().when(usuarioRepository).deleteById(id);

        // Llamamos al servicio para eliminar el usuario
        String mensaje = usuarioService.delete(id);

        // Verificamos que el mensaje de eliminación sea el esperado
        assertEquals("Usuario eliminado con éxito", mensaje);

        // Verificamos que el método deleteById del repositorio haya sido llamado exactamente una vez
        verify(usuarioRepository, times(1)).deleteById(id);
    }

}
