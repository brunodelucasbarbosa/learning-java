package br.com.loja.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produtos")
public class Produto {
  // Mapeando a classe produto com a tabela produto


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nome;
  @Column(name = "descricao")
  private String descricao;
  private BigDecimal preco;
  private LocalDate dataCadastro = LocalDate.now();

  @ManyToOne
  private Categoria categoria;



  // --------------------------------------

  public Produto() {}

  public Produto(String nome, String descricao, BigDecimal preco, Categoria categoria) {
    this.nome = nome;
    this.descricao = descricao;
    this.preco = preco;
    this.categoria = categoria;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getPreco() {
    return preco;
  }

  public void setPreco(BigDecimal preco) {
    this.preco = preco;
  }

}
