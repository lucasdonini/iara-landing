package com.controller;

import com.dao.PlanoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Plano;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.util.PGInterval;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet("/area-restrita/planos")
public class PlanoServlet extends HttpServlet {

  private static final String PAGINA_PRINCIPAL = "jsp/planos.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-plano.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-plano.jsp";
  private static final String PAGINA_ERRO = "/html/erro.html";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String action = req.getParameter("action");
    action = (action == null ? "read" : action.trim());

    boolean erro = true;
    String destino = null;

    try {
      switch (action) {
        case "read" -> {
          List<Plano> planos = listaPlanos(req);
          req.setAttribute("planos", planos);
          destino = PAGINA_PRINCIPAL;
        }

        case "update" -> {
          Plano plano = getInformacoesAlteraveis(req);
          req.setAttribute("plano", plano);
          destino = PAGINA_EDICAO;
        }

        case "create" -> destino = PAGINA_CADASTRO;
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    } catch (SQLException e) {
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    if (erro) {
      resp.sendRedirect(req.getContextPath() + PAGINA_ERRO);

    } else {
      req.getRequestDispatcher(destino).forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

    String action = req.getParameter("action").trim();

    String destino = PAGINA_ERRO;

    try {
      switch (action) {
        case "create" -> registrarPlano(req);
        case "update" -> atualizarPlano(req);
        case "delete" -> removerPlano(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      destino = req.getServletPath();

    }
    // Se houver alguma exceção de JSP, aciona o método doGet
    catch (ExcecaoDeJSP e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

    } catch (SQLException e) {
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    resp.sendRedirect(req.getContextPath() + destino);
  }

  // === CREATE ===
  private void registrarPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {

    String nome = req.getParameter("nome").trim();
    String descricao = req.getParameter("descricao").trim();

    String temp = req.getParameter("valor").trim();

    // Verifica se o valor foi preenchido
    if (temp.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("valor");
    }

    double valor = Double.parseDouble(temp);

    temp = req.getParameter("anos_duracao").trim();
    int anosDuracao = temp.isBlank() ? 0 : Integer.parseInt(temp);

    temp = req.getParameter("meses_duracao").trim();
    int mesesDuracao = temp.isBlank() ? 0 : Integer.parseInt(temp);

    temp = req.getParameter("dias_duracao").trim();
    int diasDuracao = temp.isBlank() ? 0 : Integer.parseInt(temp);

    // --- Validação de dados ---
    if (anosDuracao < 0) {
      throw ExcecaoDeJSP.valorInvalido("anos duração");
    }

    if (mesesDuracao < 0) {
      throw ExcecaoDeJSP.valorInvalido("meses duração");
    }

    if (diasDuracao < 0) {
      throw ExcecaoDeJSP.valorInvalido("dias duração");
    }

    if (nome.length() > 100) {
      throw ExcecaoDeJSP.textoMuitoLongo("nome");

    } else if (nome.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("nome");
    }

    if (valor < 0) {
      throw ExcecaoDeJSP.valorInvalido("valor");
    }

    PGInterval duracao = new PGInterval(anosDuracao, mesesDuracao, diasDuracao, 0, 0, 0);

    Plano plano = new Plano(null, nome, valor, descricao, duracao);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Verifica se o cadastro não viola a constraint UNIQUE de 'nome' em 'plano'
      if (dao.pesquisarPorNome(nome) != null) {
        throw ExcecaoDeJSP.nomeDuplicado();
      }

      dao.cadastrar(plano);
    }
  }

  // === READ ===
  private List<Plano> listaPlanos(HttpServletRequest req) throws SQLException, ClassNotFoundException {

    String campoFiltro = req.getParameter("campo_filtro");
    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");
    String valorFiltro = req.getParameter("valor_filtro");


    try (PlanoDAO dao = new PlanoDAO()) {
      Object valorFiltroConvertido = dao.converterValor(campoFiltro, valorFiltro);

      // Se o campo de filtro for duracao, recupera dados de parâmetros isolados e refaz a conversão do valor
      if (Objects.equals(campoFiltro, "duracao")) {
        String labelDuracao = req.getParameter("label_duracao");
        String interval = "%s %s".formatted(valorFiltro, labelDuracao);
        valorFiltroConvertido = dao.converterValor(campoFiltro, interval);
      }

      return dao.listar(campoFiltro, valorFiltroConvertido, campoSequencia, direcaoSequencia);
    }
  }

  // === UPDATE ===
  private void atualizarPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    String nome = req.getParameter("nome").trim();
    String descricao = req.getParameter("descricao").trim();

    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    temp = req.getParameter("valor").trim();
    // Verifica se o valor foi preenchido
    if (temp.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("valor");
    }
    double valor = Double.parseDouble(temp);

    temp = req.getParameter("anos_duracao").trim();
    int anosDuracao = temp.isBlank() ? 0 : Integer.parseInt(temp);

    temp = req.getParameter("meses_duracao").trim();
    int mesesDuracao = temp.isBlank() ? 0 : Integer.parseInt(temp);

    temp = req.getParameter("dias_duracao").trim();
    int diasDuracao = temp.isBlank() ? 0 : Integer.parseInt(temp);

    // --- Validação de dados ---
    if (anosDuracao < 0) {
      throw ExcecaoDeJSP.valorInvalido("anos duração");
    }

    if (mesesDuracao < 0) {
      throw ExcecaoDeJSP.valorInvalido("meses duração");
    }

    if (diasDuracao < 0) {
      throw ExcecaoDeJSP.valorInvalido("dias duração");
    }

    if (nome.length() > 100) {
      throw ExcecaoDeJSP.textoMuitoLongo("nome");

    } else if (nome.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("nome");
    }

    if (valor < 0) {
      throw ExcecaoDeJSP.valorInvalido("valor");
    }

    PGInterval duracao = new PGInterval(anosDuracao, mesesDuracao, diasDuracao, 0, 0, 0);

    Plano alterado = new Plano(id, nome, valor, descricao, duracao);

    try (PlanoDAO dao = new PlanoDAO()) {
      Plano original = dao.getCamposAlteraveis(id);

      // Verifica se as alterações não violam a chave UNIQUE de 'nome' em 'plano'
      Plano teste = dao.pesquisarPorNome(nome);
      if (teste != null && id != teste.getId()) {
        throw ExcecaoDeJSP.nomeDuplicado();
      }

      dao.atualizar(original, alterado);
    }
  }

  // === DELETE ===
  private void removerPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PlanoDAO dao = new PlanoDAO()) {
      dao.remover(id);
    }
  }

  // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
  private Plano getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PlanoDAO dao = new PlanoDAO()) {
      return dao.pesquisarPorId(id);
    }
  }
}
