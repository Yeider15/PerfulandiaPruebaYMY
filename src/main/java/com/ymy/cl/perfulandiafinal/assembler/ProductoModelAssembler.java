package com.ymy.cl.perfulandiafinal.assembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinController.ProductoControllerV2;
import com.ymy.cl.perfulandiafinal.Inventario.YaquelinModel.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
@Component



public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoControllerV2.class).obtenerProductoPorId(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withRel("productos"));
    }



}
