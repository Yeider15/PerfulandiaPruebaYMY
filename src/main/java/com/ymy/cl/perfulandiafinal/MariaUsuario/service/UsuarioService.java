package com.ymy.cl.perfulandiafinal.MariaUsuario.service;

import com.ymy.cl.perfulandiafs.usuario.model.Usuario;
import com.ymy.cl.perfulandiafs.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public String save(Usuario usuario) {
        usuarioRepository.save(usuario);
        return "Usuario creado o actualizado con éxito";
    }

    public String delete(Integer id) {
        Usuario usuario = findById(id);  // Verificamos si el usuario existe
        usuarioRepository.delete(usuario);  // Eliminar el usuario
        return "Usuario eliminado con éxito";
    }
}
