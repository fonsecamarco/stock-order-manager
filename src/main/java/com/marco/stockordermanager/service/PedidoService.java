package com.marco.stockordermanager.service;

import com.marco.stockordermanager.dto.ItemPedidoRequestDTO;
import com.marco.stockordermanager.dto.ItemPedidoResponseDTO;
import com.marco.stockordermanager.dto.PedidoRequestDTO;
import com.marco.stockordermanager.dto.PedidoResponseDTO;
import com.marco.stockordermanager.exceptions.RegraDeNegocioException;
import com.marco.stockordermanager.model.ItemPedido;
import com.marco.stockordermanager.model.Pedido;
import com.marco.stockordermanager.model.Produto;
import com.marco.stockordermanager.repository.PedidoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
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
                throw new RegraDeNegocioException("Estoque insuficiente para o produto: " + produto.getNome());
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
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return converterParaDTO(pedidoSalvo);
    }

    public List<PedidoResponseDTO> buscarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado com o ID: " + id));
    }

    public PedidoResponseDTO buscarPorIdDTO(Long id) {
        Pedido pedido = buscarPorId(id);
        return converterParaDTO(pedido);
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        if ("CANCELADO".equalsIgnoreCase(pedido.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este pedido já está cancelado.");
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + item.getQuantidade());
        }

        pedido.setStatus("CANCELADO");
        pedidoRepository.save(pedido);
    }

    private PedidoResponseDTO converterParaDTO(Pedido pedido) {
        List<ItemPedidoResponseDTO> itensDTO = pedido.getItens().stream()
                .map(item -> new ItemPedidoResponseDTO(
                        item.getId(),
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDataCriacao(),
                pedido.getValorTotal(),
                pedido.getStatus(),
                itensDTO
        );
    }
}