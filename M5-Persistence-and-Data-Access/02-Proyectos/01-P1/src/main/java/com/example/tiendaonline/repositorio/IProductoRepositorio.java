package com.example.tiendaonline.repositorio;

import com.example.tiendaonline.modelo.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IProductoRepositorio extends CrudRepository<Producto, Long>, PagingAndSortingRepository<Producto, Long> {

    // Consulta derivada del nombre: buscar por nombre
    Optional<Producto> findById(Long id);

    // Consulta derivada: buscar productos creados entre dos fechas
    List<Producto> findByFechaCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Consulta personalizada con @Query: buscar nombres que contengan una parte
    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %?1%")
    List<Producto> findByNombreContiene(String parteNombre);

    // Método de PagingAndSortingRepository para paginación
    Page<Producto> findAll(Pageable paginable);
}
