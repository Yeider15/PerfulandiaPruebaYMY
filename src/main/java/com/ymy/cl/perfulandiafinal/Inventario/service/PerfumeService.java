package com.ymy.cl.perfulandiafinal.Inventario.service;
import com.ymy.cl.perfulandiafinal.Inventario.model.PerfumeModel;
import com.ymy.cl.perfulandiafinal.Inventario.repository.PerfumeRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class PerfumeService {
    @Autowired
    private PerfumeRepository perfumeRepository;

    public List<PerfumeModel> findAll() {
        return perfumeRepository.findAll();
    }

    public PerfumeModel findById(Integer id) {
        return perfumeRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfume no encontrado"));
    }

    public String save(PerfumeModel perfume) {
        perfumeRepository.save(perfume);
        return "Perfume creado o actualizado con éxito";
    }

    public String delete(Integer id) {
        perfumeRepository.deleteById(id);
        return "Perfume eliminado con éxito";
    }

    public String updateStock(Integer id, int cantidadVendida) {
        PerfumeModel perfume = findById(id);
        int nuevoStock = perfume.getStock() - cantidadVendida;

        if (nuevoStock < 0) {
            throw new RuntimeException("No hay suficiente stock para el perfume: " + perfume.getNombre());
        }

        perfume.setStock(nuevoStock);
        perfumeRepository.save(perfume);
        return "Stock actualizado con éxito para el perfume: " + perfume.getNombre();
    }
}
