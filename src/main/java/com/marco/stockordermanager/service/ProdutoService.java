package com.marco.stockordermanager.service;

import com.marco.stockordermanager.dto.ProdutoRequestDTO;
import com.marco.stockordermanager.exceptions.RegraDeNegocioException;
import com.marco.stockordermanager.exceptions.ResourceNotFoundException;
import com.marco.stockordermanager.model.Produto;
import com.marco.stockordermanager.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<Produto> listAll() {
        return produtoRepository.findAll();
    }

    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Produto save(ProdutoRequestDTO dto) {

        if (produtoRepository.existsByNome(dto.nome())) {
            throw new RegraDeNegocioException("Já existe um produto cadastrado com esse nome.");
        }

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());

        return produtoRepository.save(produto);
    }

    public Produto update(Long id, ProdutoRequestDTO dto) {
        Produto produtoExistente = findById(id);

        if (produtoRepository.existsByNomeAndIdNot(dto.nome(), id)) {
            throw new RegraDeNegocioException("Já existe outro produto cadastrado com esse nome.");
        }
        produtoExistente.setNome(dto.nome());
        produtoExistente.setPreco(dto.preco());
        produtoExistente.setQuantidadeEstoque(dto.quantidadeEstoque());

        return produtoRepository.save(produtoExistente);
    }

    public void delete(Long id) {
        Produto produto = findById(id);
        produtoRepository.delete(produto);
    }

    public Produto darBaixaEstoque(Long id, Integer quantidade) {
        Produto produto = findById(id);

        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new RegraDeNegocioException("Estoque insuficiente.");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        return  produtoRepository.save(produto);
    }

    public Produto reporEstoque(Long id, Integer quantidade) {
        Produto produto = findById(id);

        if (quantidade <= 0) {
            throw new RegraDeNegocioException("A quantidade para reposição deve ser maior que zero.");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        return produtoRepository.save(produto);

    }
}