package com.controller;

import com.dao.PlanoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Plano;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/planos")
public class PlanoServlet extends HttpServlet {
  private static final String PAGINA_PRINCIPAL = "WEB-INF/jsp/planos.jsp";
  private static final String PAGINA_CADASTRO = "WEB-INF/jsp/cadastro-plano.jsp";
  private static final String PAGINA_EDICAO = "WEB-INF/jsp/editar-plano.jsp";
  private static final String PAGINA_ERRO = "html/erro.html";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Dados da requisição
    String action = req.getParameter("action").trim();
    Object origem = req.getAttribute("origem");

    if ("post".equals(origem)) {
      action = "read";
    }

    // Dados da resposta
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
      // Se houver alguma exceção, registra no terminal
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    // Redireciona a request par a página jsp
    if (erro) {
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);
    } else {
      RequestDispatcher rd = req.getRequestDispatcher(destino);
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String action = req.getParameter("action").trim();

    // Dados da resposta
    boolean erro = true;

    try {
      switch (action) {
        case "create" -> registrarPlano(req);
        case "update" -> atualizarPlano(req);
        case "delete" -> removerPlano(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    } catch (ExcecaoDeJSP e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

    } catch (SQLException e) {
      // Se houver alguma exceção grave, registra no terminal
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    // Redireciona para a página de destino
    if (erro) {
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);

    } else {
      req.setAttribute("origem", "post");
      doGet(req, resp);
    }
  }

  private List<Plano> listaPlanos(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    try (PlanoDAO dao = new PlanoDAO()) {
      //Dados da requisição
      String campoFiltro = req.getParameter("campoFiltro");
      String temp = req.getParameter("valorFiltro");
      Object valorFiltro = dao.converterValor(campoFiltro, temp);
      String campoSequencia = req.getParameter("campoSequencia");
      String direcaoSequencia = req.getParameter("direcaoSequencia");

      // Recupera os planos do banco
      return dao.listarPlanos(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
    }
  }

  private Plano getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Recupera os dados originais para display
      return dao.getPlanoById(id);
    }
  }

  private void registrarPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String temp = req.getParameter("valor").trim();
    if (temp.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("valor");
    }
    double valor = Double.parseDouble(temp);

    String nome = req.getParameter("nome").trim();
    String descricao = req.getParameter("descricao").trim();
    Plano plano = new Plano(null, nome, valor, descricao);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Verifica se o novo plano não viola a chave UNIQUE
      if (dao.getPlanoByNome(nome) != null) {
        throw ExcecaoDeJSP.nomeDuplicado();
      }

      // Cadastra o plano
      dao.cadastrar(plano);
    }
  }

  private void removerPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Deleta o plano
      dao.remover(id);
    }
  }

  private void atualizarPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    String nome = req.getParameter("nome").trim();

    temp = req.getParameter("valor").trim();
    if (temp.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("valor");
    }
    double valor = Double.parseDouble(temp);

    String descricao = req.getParameter("descricao").trim();
    Plano alterado = new Plano(id, nome, valor, descricao);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Recupera as informações originais do banco
      Plano original = dao.getCamposAlteraveis(id);

      // Verifica se o novo nome viola a chave UNIQUE
      Plano teste = dao.getPlanoByNome(nome);
      if (teste != null && id != teste.getId()) {
        throw ExcecaoDeJSP.nomeDuplicado();
      }

      // Salva as informações no banco
      dao.atualizar(original, alterado);
    }
  }
}
