package com.jb.supermercado.internal.produto.service;

import com.jb.supermercado.config.exception.BusinessException;
import com.jb.supermercado.config.exception.RecursoNaoEncontradoException;
import com.jb.supermercado.internal.produto.dto.ProdutoRequest;
import com.jb.supermercado.internal.produto.dto.ProdutoResponse;
import com.jb.supermercado.internal.produto.entity.ProdutoEntity;
import com.jb.supermercado.internal.produto.mapper.ProdutoMapper;
import com.jb.supermercado.internal.produto.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void cadastrarProduto(ProdutoRequest produtoRequest) {
        if (this.produtoRepository.existsByNome(produtoRequest.nome())) {
            throw new BusinessException("Já existe um produto cadastrado com este nome: " + produtoRequest.nome());
        }
        ProdutoEntity produtoEntity = ProdutoMapper.requestParaEntidade(produtoRequest);
        this.produtoRepository.save(produtoEntity);
    }

    public List<ProdutoResponse> listaProdutos() {
        List<ProdutoEntity> produtos = this.produtoRepository.findAll();
        return ProdutoMapper.entidadeParaResponseList(produtos);
    }

    public ProdutoResponse buscarProdutoPorId(Long id) {
        ProdutoEntity produtoEntity = this.produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id));
        return ProdutoMapper.entidadeParaResponse(produtoEntity);
    }

    public void atualizarProduto(Long id, ProdutoRequest produtoRequest) {
        ProdutoEntity produtoEntity = this.produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id));

        produtoEntity.setNome(produtoRequest.nome());
        produtoEntity.setDescricao(produtoRequest.descricao());
        produtoEntity.setPreco(produtoRequest.preco());
        produtoEntity.setQuantidadeEstoque(produtoRequest.quantidadeEstoque());
        produtoEntity.setStatus(produtoRequest.status());

        this.produtoRepository.save(produtoEntity);
    }

    public void removerProduto(Long id) {
        if (!this.produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado para exclusão com o ID: " + id);
        }
        this.produtoRepository.deleteById(id);
    }
}
