package leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.service.EnviadorDeEmails;
import br.com.alura.leilao.service.FinalizarLeilaoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FinalizarLeilaoServiceTest {

  private FinalizarLeilaoService service;
  @Mock
  private LeilaoDao leilaoDao;
  @Mock
  private EnviadorDeEmails enviador;

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.initMocks(this);
    this.service = new FinalizarLeilaoService(leilaoDao, enviador);
  }

  @Test
  void deveriaFinalizarUmLeilao() {
    List<Leilao> leiloes = leiloes();

    Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

    service.finalizarLeiloesExpirados();

    Leilao leilao = leiloes.get(0);
    Assertions.assertTrue(leilao.isFechado());
    Assertions.assertEquals(new BigDecimal("900"),
            leilao.getLanceVencedor().getValor());

    Mockito.verify(leilaoDao).salvar(leilao);
  }

  @Test
  void deveriaEnviarEmail() {
    List<Leilao> leiloes = leiloes();

    Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

    service.finalizarLeiloesExpirados();

    Leilao leilao = leiloes.get(0);
    Lance lanceVencedor = leilao.getLanceVencedor();

    Mockito.verify(enviador)
            .enviarEmailVencedorLeilao(lanceVencedor);
  }

  @Test
  void naoDeveriaEnviarEmailParaVencedorCasoErro() {
    List<Leilao> leiloes = leiloes();

    Mockito.when(leilaoDao.buscarLeiloesExpirados())
            .thenReturn(leiloes);

    Mockito.when(leilaoDao.salvar(Mockito.any(Leilao.class)))
            .thenThrow(RuntimeException.class);

    try {
      service.finalizarLeiloesExpirados();
      Mockito.verifyNoInteractions(enviador);
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }

  }

  private List<Leilao> leiloes() {
    List<Leilao> lista = new ArrayList<>();

    Leilao leilao = new Leilao("Celular",
            new BigDecimal("500"),
            new Usuario("Fulano"));

    Lance primeiro = new Lance(new Usuario("Beltrano"),
            new BigDecimal("600"));
    Lance segundo = new Lance(new Usuario("Ciclano"),
            new BigDecimal("900"));

    leilao.propoe(primeiro);
    leilao.propoe(segundo);

    lista.add(leilao);

    return lista;

  }
}
