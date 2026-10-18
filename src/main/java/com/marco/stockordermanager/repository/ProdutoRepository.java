package com.marco.stockordermanager.repository;

import com.marco.stockordermanager.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);
}