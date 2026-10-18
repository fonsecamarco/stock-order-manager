package com.marco.stockordermanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        LocalDateTime dataCriacao,
        BigDecimal valorTotal,
        String status,
        List<ItemPedidoResponseDTO> itens
) {}