package com.controller;

import com.dao.FabricaDAO;
import com.dao.PagamentoDAO;
import com.exception.ExcecaoDePagina;
import com.model.Pagamento;
import jakarta.servlet.RequestDispatcher;
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
  private static final String PAGINA_PRINCIPAL = "WEB-INF/jsp/pagamentos.jsp";
  private static final String PAGINA_CADASTRO = "WEB-INF/jsp/cadastro-pagamento.jsp";
  private static final String PAGINA_EDICAO = "WEB-INF/jsp/editar-pagamento.jsp";
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
        case "create" -> registrarPagamento(req);
        case "update" -> atualizarPagamento(req);
        case "delete" -> removerPagamento(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    } catch (ExcecaoDePagina e) {
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

  private List<Pagamento> listarPagamentos(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    try (PagamentoDAO dao = new PagamentoDAO()) {
      //Dados da requisição
      String campoFiltro = req.getParameter("campoFiltro");
      String temp = req.getParameter("valorFiltro");
      Object valorFiltro = dao.converterValor(campoFiltro, temp);
      String campoSequencia = req.getParameter("campoSequencia");
      String direcaoSequencia = req.getParameter("direcaoSequencia");

      // Recupera os planos do banco
      return dao.listarPagamentos(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
    }
  }

  private Pagamento getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Recupera os dados originais para display
      return dao.getPagamentoById(id);
    }
  }

  private void registrarPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    temp = req.getParameter("dataVencimento").trim();
    LocalDate dataVencimento = LocalDate.parse(temp);

    LocalDate dataPagamento = null;
    if (status) {
      temp = req.getParameter("dataPagamento").trim();
      if (temp.isBlank()) {
        throw ExcecaoDePagina.campoNecessarioFaltante("Data do Pagamento");
      }

      dataPagamento = LocalDate.parse(temp);
    }

    String tipoPagamento = req.getParameter("tipoPagamento").trim();

    temp = req.getParameter("fkFabrica").trim();
    int fkFabrica = Integer.parseInt(temp);

    Pagamento pagamento = new Pagamento(null, null, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Cadastra o pagamento
      dao.cadastrar(pagamento);
    }
  }

  private void removerPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Deleta o plano
      dao.remover(id);
    }
  }

  private void atualizarPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    temp = req.getParameter("valorPago").trim();
    double valorPago = Double.parseDouble(temp);

    temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    temp = req.getParameter("dataVencimento").trim();
    LocalDate dataVencimento = LocalDate.parse(temp);

    temp = req.getParameter("dataPagamento").trim();
    LocalDate dataPagamento = LocalDate.parse(temp);

    String tipoPagamento = req.getParameter("tipoPagamento").trim();

    temp = req.getParameter("fkFabrica").trim();
    int fkFabrica = Integer.parseInt(temp);

    Pagamento alterado = new Pagamento(id, valorPago, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);

    try (PagamentoDAO dao = new PagamentoDAO()) {
      // Recupera as informações originais do banco
      Pagamento original = dao.getCamposAlteraveis(id);

      // Salva as informações no banco
      dao.atualizar(original, alterado);
    }
  }

  private Map<Integer, String> getMapFabricas() throws SQLException, ClassNotFoundException {
    try (FabricaDAO dao = new FabricaDAO()) {
      return dao.getMapIdNome();
    }
  }
}
