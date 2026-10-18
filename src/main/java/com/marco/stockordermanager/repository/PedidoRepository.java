package com.marco.stockordermanager.repository;

import com.marco.stockordermanager.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}