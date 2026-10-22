package com.marco.stockordermanager.controller;

import com.marco.stockordermanager.dto.ProdutoRequestDTO;
import com.marco.stockordermanager.model.Produto;
import com.marco.stockordermanager.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public List<Produto> listAll(){
        return produtoService.listAll();
    }

    @GetMapping("/{id}")
    public Produto findById(@PathVariable Long id){
        return produtoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save(@RequestBody @Valid ProdutoRequestDTO dto) {
        return produtoService.save(dto);
    }

    @PutMapping("/{id}")
    public Produto update(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        return produtoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        produtoService.delete(id);
    }

    @PutMapping("/{id}/baixa-estoque")
    public Produto darBaixaEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        return produtoService.darBaixaEstoque(id, quantidade);
    }

    @PutMapping("/{id}/repor-estoque")
    public Produto reporEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        return produtoService.reporEstoque(id, quantidade);
    }
}
