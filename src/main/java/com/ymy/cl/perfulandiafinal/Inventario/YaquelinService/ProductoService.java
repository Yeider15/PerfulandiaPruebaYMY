package com.ymy.cl.perfulandiafinal.Inventario.YaquelinService;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinRepository.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Integer id) {
        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public String save(Producto producto) {
        productoRepository.save(producto);
        return "Producto creado o actualizado con éxito";
    }

    public String delete(Integer id) {
        productoRepository.deleteById(id);
        return "Producto eliminado con éxito";
    }

    public String updateStock(Integer id, int cantidadVendida) {
        Producto producto = findById(id);
        int nuevoStock = producto.getStock() - cantidadVendida;

        if (nuevoStock < 0) {
            throw new RuntimeException("No hay suficiente stock de producto: " + producto.getNombre());
        }

        producto.setStock(nuevoStock);
        productoRepository.save(producto);
        return "Stock actualizado con éxito para el producto: " + producto.getNombre();
    }
}
