package com.example.tiendaonline.repositorio;

import com.example.tiendaonline.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface IPedidoRepositorio extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.productos WHERE p.id = :id")
    Optional<Pedido> findByIdWithProductos(@Param("id") Long id);
}
