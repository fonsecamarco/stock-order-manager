package com.marco.stockordermanager.dto;

public record ItemPedidoResponseDTO(
        Long id,
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        Double precoUnitario
) {}