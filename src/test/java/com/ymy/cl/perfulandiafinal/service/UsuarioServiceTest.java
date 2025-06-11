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
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario(1, "Juan Pérez", "Lopez", 123456789, "juan.perez@email.com", "Calle Falsa 123", new Date(), null)));

        List<Usuario> usuarios = usuarioService.findAll();

        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("Juan Pérez", usuarios.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Usuario usuario = new Usuario(id, "Juan Pérez", "Lopez", 123456789, "juan.perez@email.com", "Calle Falsa 123", new Date(), null);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario found = usuarioService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Juan Pérez", found.getNombre());
    }

    @Test
    public void testSave() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Juan Pérez");
        usuario.setApellido("Lopez");
        usuario.setTelefono(123456789);
        usuario.setCorreo("juan.perez@email.com");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setFechaRegistro(new Date());

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        String resultado = usuarioService.save(usuario);

        assertNotNull(resultado);
        assertEquals("Usuario creado o actualizado con éxito", resultado);

        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        Usuario usuario = new Usuario(id, "Juan Pérez", "Lopez", 123456789, "juan.perez@email.com", "Calle Falsa 123", new Date(), null);

        // Simula que el repositorio devuelve el usuario al buscarlo
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Simula que el repositorio elimina el usuario pasando el objeto completo
        doNothing().when(usuarioRepository).delete(usuario);  // Usamos delete() en lugar de deleteById

        // Llamamos al servicio para eliminar el usuario
        String mensaje = usuarioService.delete(id);

        // Verificamos que el mensaje de eliminación sea el esperado
        assertEquals("Usuario eliminado con éxito", mensaje);

        // Verificamos que el método delete haya sido llamado exactamente una vez con el objeto usuario
        verify(usuarioRepository, times(1)).delete(usuario);

        // Caso 2: El usuario no existe
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Llamamos al servicio para intentar eliminar un usuario no existente
        Exception exception = assertThrows(RuntimeException.class, () -> usuarioService.delete(id));

        // Verificamos que la excepción haya sido lanzada con el mensaje esperado
        assertEquals("Usuario no encontrado", exception.getMessage());

        // Verificamos que el método delete no haya sido llamado en este caso
        verify(usuarioRepository, times(1)).delete(usuario);
    }



}
