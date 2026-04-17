package com.jb.supermercado.internal.produto.repository;

import com.jb.supermercado.internal.produto.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    boolean existsByNome(String nome);
}
