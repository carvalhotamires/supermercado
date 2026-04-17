package com.jb.supermercado.internal.produto.mapper;

import com.jb.supermercado.internal.produto.dto.ProdutoRequest;
import com.jb.supermercado.internal.produto.dto.ProdutoResponse;
import com.jb.supermercado.internal.produto.entity.ProdutoEntity;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoMapper {

    public static ProdutoEntity requestParaEntidade(ProdutoRequest produtoRequest) {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setNome(produtoRequest.nome());
        produtoEntity.setDescricao(produtoRequest.descricao());
        produtoEntity.setPreco(produtoRequest.preco());
        produtoEntity.setQuantidadeEstoque(produtoRequest.quantidadeEstoque());
        produtoEntity.setStatus(produtoRequest.status());
        return produtoEntity;
    }

    public static ProdutoResponse entidadeParaResponse(ProdutoEntity produtoEntity) {
        return new ProdutoResponse(
                produtoEntity.getId(),
                produtoEntity.getNome(),
                produtoEntity.getDescricao(),
                produtoEntity.getPreco(),
                produtoEntity.getQuantidadeEstoque(),
                produtoEntity.getStatus()
        );
    }

    public static List<ProdutoResponse> entidadeParaResponseList(List<ProdutoEntity> produtoEntityList) {
        return produtoEntityList.stream()
                .map(ProdutoMapper::entidadeParaResponse)
                .collect(Collectors.toList());
    }
}
