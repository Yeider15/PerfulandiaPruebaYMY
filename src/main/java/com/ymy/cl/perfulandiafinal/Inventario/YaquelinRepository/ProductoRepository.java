package com.ymy.cl.perfulandiafinal.Inventario.YaquelinRepository;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Buscar perfume por su nombre
    Producto findByNombre(String nombre);
}
