package com.marco.stockordermanager.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        LocalDateTime dataCriacao,
        Double valorTotal,
        String status,
        List<ItemPedidoResponseDTO> itens
) {}