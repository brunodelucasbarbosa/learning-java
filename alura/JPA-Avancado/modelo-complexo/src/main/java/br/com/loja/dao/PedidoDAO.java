package br.com.loja.dao;

import br.com.loja.model.Pedido;
import br.com.loja.VO.RelatorioDeVendasVO;

import javax.persistence.EntityManager;



import java.math.BigDecimal;
import java.util.List;

public class PedidoDAO {

  private EntityManager em;

  public PedidoDAO(EntityManager em) {
    this.em = em;
  }

  public void cadastrar(Pedido pedido) {
    this.em.persist(pedido);
  }

  public BigDecimal valorTotalVendido() {
    String jpql = "SELECT SUM(p.valorTotal) FROM Pedido p";
    return em.createQuery(jpql, BigDecimal.class)
            .getSingleResult();
  }

  public List<RelatorioDeVendasVO> relatorioDeVendas() {
    String jpql = "SELECT new br.com.loja.VO.RelatorioDeVendasVO(" +
            "produto.nome," +
            "SUM(item.quantidade), " +
            "MAX(pedido.data)) " +
            "FROM Pedido pedido " +
            "JOIN pedido.itens item " +
            "JOIN item.produto produto " +
            "GROUP BY produto.nome " +
            "ORDER BY item.quantidade DESC ";

    return em.createQuery(jpql, RelatorioDeVendasVO.class).getResultList();
  }
}
