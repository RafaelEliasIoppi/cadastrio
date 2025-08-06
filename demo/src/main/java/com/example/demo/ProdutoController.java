package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // 🔍 Buscar todos os produtos
    @GetMapping
    public List<Produto> listarTodos(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return produtoRepository.findByNomeContainingIgnoreCase(nome);
        }
        return produtoRepository.findAll();
    }

    // 📝 Criar novo produto
    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    // ✏️ Atualizar produto por ID
    @PutMapping("/{id}")
    public Produto atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        return produtoRepository.findById(id)
            .map(produto -> {
                produto.setNome(produtoAtualizado.getNome());
                produto.setPreco(produtoAtualizado.getPreco());
                return produtoRepository.save(produto);
            })
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }

    // 🗑️ Remover produto por ID
    @DeleteMapping("/{id}")
    public void removerProduto(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }
}