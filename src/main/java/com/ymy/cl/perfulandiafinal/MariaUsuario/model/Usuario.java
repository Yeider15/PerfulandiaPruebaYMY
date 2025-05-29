package com.ymy.cl.perfulandiafinal.MariaUsuario.model;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ymy.cl.perfulandiafinal.YeiderVentas.model.Venta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private Integer telefono;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Date fechaRegistro;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuario")
    private List<Venta> ventas; // Un usuario puede realizar muchas ventas

}

