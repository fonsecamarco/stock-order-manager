package com.marco.stockordermanager.service;

import com.marco.stockordermanager.dto.ProdutoRequestDTO;
import com.marco.stockordermanager.exceptions.ResourceNotFoundException;
import com.marco.stockordermanager.model.Produto;
import com.marco.stockordermanager.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @Test
    @DisplayName("Deve retornar um produto quando o ID existir")
    void buscarPorIdComSucesso() {
        Long idExistente = 1L;
        Produto produto = new Produto();
        produto.setId(idExistente);
        produto.setNome("Teclado Mecânico");

        when(repository.findById(idExistente)).thenReturn(Optional.of(produto));

        var resultado = service.findById(idExistente);

        assertNotNull(resultado);
        assertEquals("Teclado Mecânico", resultado.getNome());
        verify(repository, times(1)).findById(idExistente);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID não existir")
    void buscarPorIdComErroQuandoNaoEncontrado() {
        Long idInexistente = 99L;
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(idInexistente);
        });

        verify(repository, times(1)).findById(idInexistente);
    }
    @Test
    @DisplayName("Deve salvar e retornar o produto com sucesso")
    void salvarComSucesso() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Mouse Gamer", new BigDecimal("150.00"), 10);

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Mouse Gamer");
        produtoSalvo.setPreco(new BigDecimal("150.00"));

        when(repository.save(any(Produto.class))).thenReturn(produtoSalvo);

        var resultado = service.save(dto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Mouse Gamer", resultado.getNome());
        verify(repository, times(1)).save(any(Produto.class));
    }
}