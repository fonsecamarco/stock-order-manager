package com.marco.stockordermanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProdutoResponseDTO(
        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        @Valid
        List<ItemPedidoRequestDTO> itens
) {}

