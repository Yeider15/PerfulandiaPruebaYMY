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

@SpringBootTest
public class UsuarioServiceTest {

    // Inyecta el servicio de Usuario para ser probado.
    @Autowired
    private UsuarioService usuarioService;

    // Crea un mock del repositorio de Usuario para simular su comportamiento.
    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindAll() {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con un Usuario.
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Juan Perez");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        // Llama al método findAll() del servicio.
        List<Usuario> usuarios = usuarioService.findAll();

        // Verifica que la lista devuelta no sea nula y que contenga exactamente un Usuario.
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("Juan Perez", usuarios.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Juan Perez");

        // Define el comportamiento del mock: cuando se llame a findById(), devuelve un Usuario.
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Llama al método findById() del servicio.
        Usuario foundUsuario = usuarioService.findById(id);

        // Verifica que el Usuario devuelto no sea nulo y que su nombre coincida con el esperado.
        assertNotNull(foundUsuario);
        assertEquals(id, foundUsuario.getId());
        assertEquals("Juan Perez", foundUsuario.getNombre());
    }

    @Test
    public void testSave() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Juan Perez");

        // Define el comportamiento del mock: cuando se llame a save(), devuelve el Usuario proporcionado.
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // Llama al método save() del servicio.
        String result = usuarioService.save(usuario);

        // Verifica que el mensaje devuelto sea el esperado.
        assertEquals("Usuario creado o actualizado con éxito", result);

        // Verifica que el método save() del repositorio haya sido llamado exactamente una vez.
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testDelete() {
        Integer id = 1;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Juan Perez");

        // Define el comportamiento del mock: cuando se llame a findById(), devuelve un Usuario.
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario); // No hace nada al eliminar.

        // Llama al método delete() del servicio.
        String result = usuarioService.delete(id);

        // Verifica que el método delete() del repositorio haya sido llamado exactamente una vez.
        verify(usuarioRepository, times(1)).delete(usuario);
        assertEquals("Usuario eliminado con éxito", result);
    }

    @Test
    public void testDeleteUsuarioNoEncontrado() {
        Integer id = 1;

        // Define el comportamiento del mock: cuando se llame a findById() con un ID que no existe, lanza una excepción.
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Verifica que la excepción sea lanzada.
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            usuarioService.delete(id);
        });

        // Verifica que el mensaje de la excepción sea el esperado.
        assertEquals("Usuario no encontrado", thrown.getMessage());
    }
}
