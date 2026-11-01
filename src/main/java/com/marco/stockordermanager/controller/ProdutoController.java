package com.marco.stockordermanager.controller;

import com.marco.stockordermanager.dto.ProdutoRequestDTO;
import com.marco.stockordermanager.dto.ProdutoResponseDTO;
import com.marco.stockordermanager.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public List<ProdutoResponseDTO> listAll() {
        return produtoService.listAll()
                .stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO findById(@PathVariable Long id) {
        return new ProdutoResponseDTO(produtoService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO save(@RequestBody @Valid ProdutoRequestDTO dto) {
        return new ProdutoResponseDTO(produtoService.save(dto));
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO update(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        return new ProdutoResponseDTO(produtoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        produtoService.delete(id);
    }

    @PutMapping("/{id}/baixa-estoque")
    public ProdutoResponseDTO darBaixaEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        return new ProdutoResponseDTO(produtoService.darBaixaEstoque(id, quantidade));
    }

    @PutMapping("/{id}/repor-estoque")
    public ProdutoResponseDTO reporEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        return new ProdutoResponseDTO(produtoService.reporEstoque(id, quantidade));
    }
}
