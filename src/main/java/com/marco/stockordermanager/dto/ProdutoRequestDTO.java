package com.marco.stockordermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequestDTO(

        @NotBlank(message = "O nome do produto não pode ser vazio.")
        String nome,

        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser maior que zero.")
        Double preco,

        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
        Integer quantidadeEstoque
) {}