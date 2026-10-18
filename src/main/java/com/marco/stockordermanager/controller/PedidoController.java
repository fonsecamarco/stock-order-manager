package com.marco.stockordermanager.controller;

import com.marco.stockordermanager.dto.PedidoRequestDTO;
import com.marco.stockordermanager.dto.PedidoResponseDTO;
import com.marco.stockordermanager.model.Pedido;
import com.marco.stockordermanager.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
