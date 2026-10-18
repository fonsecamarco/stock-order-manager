package com.marco.stockordermanager.service;

import com.marco.stockordermanager.dto.ItemPedidoRequestDTO;
import com.marco.stockordermanager.dto.PedidoRequestDTO;
import com.marco.stockordermanager.model.ItemPedido;
import com.marco.stockordermanager.model.Pedido;
import com.marco.stockordermanager.model.Produto;
import com.marco.stockordermanager.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;

    @Transactional
    public Pedido criarPedido(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus("CRIADO");

        if (pedido.getItens() == null) {
            pedido.setItens(new ArrayList<>());
        }

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDTO : dto.itens()) {
            Produto produto = produtoService.findById(itemDTO.produtoId());

            if (produto.getQuantidadeEstoque() < itemDTO.quantidade()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDTO.quantidade());

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setPedido(pedido);

            pedido.getItens().add(item);

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.quantidade()));
            valorTotal = valorTotal.add(subtotal);
        }

        pedido.setValorTotal(valorTotal);
        return pedidoRepository.save(pedido);
    }
}