package com.ymy.cl.perfulandiafinal.MariaUsuario.repository;

import com.ymy.cl.perfulandiafinal.MariaUsuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Buscar usuario por su correo
    Usuario findByCorreo(String correo);

}
