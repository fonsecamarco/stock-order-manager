package com.marco.stockordermanager.controller;

import com.marco.stockordermanager.dto.PedidoRequestDTO;
import com.marco.stockordermanager.dto.PedidoResponseDTO;
import com.marco.stockordermanager.model.Pedido;
import com.marco.stockordermanager.service.PedidoService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO criarPedido(@RequestBody @Valid PedidoRequestDTO dto){
        return pedidoService.criarPedido(dto);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(pedidoService.buscarTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedido(@PathVariable Long id){
        return ResponseEntity.ok(pedidoService.buscarPorIdDTO(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id){
        pedidoService.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }
}
