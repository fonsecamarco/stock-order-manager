package com.marco.stockordermanager.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long id,
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario
) {}