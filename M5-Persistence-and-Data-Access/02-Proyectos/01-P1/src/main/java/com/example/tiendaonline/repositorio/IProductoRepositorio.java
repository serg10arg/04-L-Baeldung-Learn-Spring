package com.example.tiendaonline.repositorio;

import com.example.tiendaonline.modelo.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IProductoRepositorio extends JpaRepository <Producto, Long> {

    // Consulta derivada del nombre: buscar por nombre
    Optional<Producto> findById(Long id);

    // Consulta derivada: buscar productos creados entre dos fechas
    List<Producto> findByFechaCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Consulta personalizada con @Query: buscar nombres que contengan una parte
    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:parteNombre%")
    List<Producto> findByNombreContiene(@Param("parteNombre") String parteNombre); // @Param para enlazar el argumento con el nombre de la consulta

    // MEJORA: Una consulta muy útil en este dominio sería buscar productos por el ID de su pedido.
    Page<Producto> findByPedidoId(Long pedidoId, Pageable pageable);

    // Método de PagingAndSortingRepository para paginación
    Page<Producto> findAll(Pageable paginable);


}
