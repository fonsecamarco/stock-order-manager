package com.marco.stockordermanager.service;

import com.marco.stockordermanager.dto.ItemPedidoRequestDTO;
import com.marco.stockordermanager.dto.PedidoRequestDTO;
import com.marco.stockordermanager.dto.PedidoResponseDTO;
import com.marco.stockordermanager.exceptions.RegraDeNegocioException;
import com.marco.stockordermanager.exceptions.ResourceNotFoundException;
import com.marco.stockordermanager.model.ItemPedido;
import com.marco.stockordermanager.model.Pedido;
import com.marco.stockordermanager.model.Produto;
import com.marco.stockordermanager.repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve criar um pedido com sucesso e abaixar o estoque do produto")
    void criarPedidoComSucesso() {
        Long produtoId = 1L;
        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Teclado Mecânico");
        produto.setPreco(new BigDecimal("200.00"));
        produto.setQuantidadeEstoque(10);

        ItemPedidoRequestDTO itemDTO = new ItemPedidoRequestDTO(produtoId, 2);
        PedidoRequestDTO pedidoDTO = new PedidoRequestDTO(List.of(itemDTO));

        when(produtoService.findById(produtoId)).thenReturn(produto);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(100L);
            return p;
        });

        PedidoResponseDTO response = pedidoService.criarPedido(pedidoDTO);

        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals("CRIADO", response.status());
        assertEquals(new BigDecimal("400.00"), response.valorTotal());
        assertEquals(8, produto.getQuantidadeEstoque()); // Baixou estoque de 10 para 8
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar RegraDeNegocioException quando o estoque for insuficiente")
    void criarPedidoComEstoqueInsuficiente() {
        Long produtoId = 1L;
        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Mouse Gamer");
        produto.setPreco(new BigDecimal("100.00"));
        produto.setQuantidadeEstoque(1); // Apenas 1 no estoque

        ItemPedidoRequestDTO itemDTO = new ItemPedidoRequestDTO(produtoId, 5); // Tentando comprar 5
        PedidoRequestDTO pedidoDTO = new PedidoRequestDTO(List.of(itemDTO));

        when(produtoService.findById(produtoId)).thenReturn(produto);

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class, () -> {
            pedidoService.criarPedido(pedidoDTO);
        });

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve retornar um pedido ao buscar por ID existente")
    void buscarPorIdComSucesso() {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus("CRIADO");

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(pedidoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar por ID inexistente")
    void buscarPorIdInexistente() {
        Long id = 99L;
        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.buscarPorId(id);
        });

        verify(pedidoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve cancelar pedido com sucesso e devoler os produtos ao estoque")
    void cancelarPedidoComSucesso() {
        Long pedidoId = 1L;

        Produto produto = new Produto();
        produto.setId(10L);
        produto.setQuantidadeEstoque(5);

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(3);

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus("CRIADO");
        pedido.setItens(new ArrayList<>(List.of(item)));

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        pedidoService.cancelarPedido(pedidoId);

        assertEquals("CANCELADO", pedido.getStatus());
        assertEquals(8, produto.getQuantidadeEstoque()); // Devolveu 3 itens ao estoque (5 + 3 = 8)
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Deve lançar RegraDeNegocioException ao tentar cancelar pedido já cancelado")
    void cancelarPedidoJaCancelado() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus("CANCELADO");

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class, () -> {
            pedidoService.cancelarPedido(pedidoId);
        });

        assertEquals("Este pedido já está cancelado.", exception.getMessage());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}