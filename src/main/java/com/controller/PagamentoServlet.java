package com.controller;

import com.dao.FabricaDAO;
import com.dao.PagamentoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Pagamento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/pagamentos")
public class PagamentoServlet extends HttpServlet {
  // Constantes
  private static final String PAGINA_PRINCIPAL = "WEB-INF/jsp/pagamentos.jsp";
  private static final String PAGINA_CADASTRO = "WEB-INF/jsp/cadastro-pagamento.jsp";
  private static final String PAGINA_EDICAO = "WEB-INF/jsp/editar-pagamento.jsp";
  private static final String PAGINA_ERRO = "html/erro.html";

  // GET e POST
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
      // Faz a ação correspondente à escolha
      switch (action) {
        case "read" -> {
          List<Pagamento> pagamentos = listarPagamentos(req);
          Map<Integer, String> fabricas = getMapFabricas();

          req.setAttribute("pagamentos", pagamentos);
          req.setAttribute("fabricas", fabricas);

          destino = PAGINA_PRINCIPAL;
        }

        case "update" -> {
          Pagamento pagamento = getInformacoesAlteraveis(req);
          Map<Integer, String> fabricas = getMapFabricas();

          req.setAttribute("pagamento", pagamento);
          req.setAttribute("fabricas", fabricas);

          destino = PAGINA_EDICAO;
        }

        case "create" -> {
          Map<Integer, String> fabricas = getMapFabricas();
          req.setAttribute("fabricas", fabricas);

          destino = PAGINA_CADASTRO;
        }

        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    }
    // Se houver alguma exceção, registra no terminal
    catch (SQLException e) {
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    // Redireciona para a página de erro, ou encaminha a requisição e a resposta
    if (erro) {
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);

    } else {
      req.getRequestDispatcher(destino).forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da requisição
    String action = req.getParameter("action").trim();

    // Dados da resposta
    boolean erro = true;

    try {
      // Fazer a ação correspondente à escolha
      switch (action) {
        case "create" -> registrarPagamento(req);
        case "update" -> atualizarPagamento(req);
        case "delete" -> removerPagamento(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    }
    // Se houver alguma exceção de JSP, aciona o método doGet
    catch (ExcecaoDeJSP e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

    }
    // Se houver alguma exceção, registra no terminal
    catch (SQLException e) {
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    // Redireciona para a página de erro, ou aciona o método doGet
    if (erro) {
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);

    } else {
      req.setAttribute("origem", "post");
      doGet(req, resp);
    }
  }

  // Outros Métodos

  // === CREATE ===
  private void registrarPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    temp = req.getParameter("data_vencimento").trim();
    LocalDate dataVencimento = LocalDate.parse(temp);

    LocalDate dataPagamento = null;
    double valor = 0;

    if (status) {
      temp = req.getParameter("data_pagamento").trim();

      if (temp.isBlank()) {
        throw ExcecaoDeJSP.campoNecessarioFaltante("Data do Pagamento");
      }

      dataPagamento = LocalDate.parse(temp);

      temp = req.getParameter("valor").trim();
      valor = Double.parseDouble(temp);
    }

    String tipoPagamento = req.getParameter("tipo_pagamento").trim();

    temp = req.getParameter("id_fabrica").trim();
    int fkFabrica = Integer.parseInt(temp);

    // Instância do Model
    Pagamento pagamento = new Pagamento(null, valor, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Cadastra o pagamento
      dao.cadastrar(pagamento);
    }
  }

  // === READ ===
  private List<Pagamento> listarPagamentos(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    //Dados da requisição
    String campoFiltro = req.getParameter("campo_filtro");
    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");
    String valorFiltro = req.getParameter("valor_filtro");

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Recupera e retorna os pagamentos cadastrados no banco de dados
      return dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
    }
  }

  private Pagamento getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Recupera e retorna os dados originais do banco de dados
      return dao.pesquisarPorId(id);
    }
  }

  private Map<Integer, String> getMapFabricas() throws SQLException, ClassNotFoundException {
    try (FabricaDAO dao = new FabricaDAO()) {
      return dao.getMapIdNome();
    }
  }

  // === UPDATE ===
  private void atualizarPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String tipoPagamento = req.getParameter("tipo_pagamento").trim();

    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    temp = req.getParameter("valor_pago").trim();
    double valorPago = Double.parseDouble(temp);

    temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    temp = req.getParameter("data_vencimento").trim();
    LocalDate dataVencimento = LocalDate.parse(temp);

    temp = req.getParameter("data_pagamento").trim();
    LocalDate dataPagamento = LocalDate.parse(temp);

    temp = req.getParameter("id_fabrica").trim();
    int fkFabrica = Integer.parseInt(temp);

    // Instância do Model
    Pagamento alterado = new Pagamento(id, valorPago, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Recupera os dados originais do banco de dados
      Pagamento original = dao.getCamposAlteraveis(id);

      // Atualiza o pagamento
      dao.atualizar(original, alterado);
    }
  }

  // === DELETE ===
  private void removerPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Deleta o pagamento
      dao.remover(id);
    }
  }
}
