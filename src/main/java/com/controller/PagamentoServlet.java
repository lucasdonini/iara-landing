package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.dao.FabricaDAO;
import com.dao.PagamentoDAO;
import com.dao.PlanoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.MetodoPagamento;
import com.model.Pagamento;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/area-restrita/pagamentos")
public class PagamentoServlet extends HttpServlet {

  private static final String PAGINA_PRINCIPAL = "jsp/pagamentos.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-pagamento.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-pagamento.jsp";
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
                    List<Pagamento> pagamentos = listarPagamentos(req);
                    Map<Integer, String> fabricas = getMapFabricas();
                    Map<Integer, String> planos = getMapPlanos();

                    req.setAttribute("pagamentos", pagamentos);
                    req.setAttribute("fabricas", fabricas);
                    req.setAttribute("planos", planos);

                    destino = PAGINA_PRINCIPAL;
                }

                case "update" -> {
                    Pagamento pagamento = getInformacoesAlteraveis(req);
                    Map<Integer, String> fabricas = getMapFabricas();
                    Map<Integer, String> planos = getMapPlanos();

                    req.setAttribute("pagamento", pagamento);
                    req.setAttribute("fabricas", fabricas);
                    req.setAttribute("planos", planos);

                    destino = PAGINA_EDICAO;
                }

                case "create" -> {
                    Map<Integer, String> fabricas = getMapFabricas();
                    Map<Integer, String> planos = getMapPlanos();

                    req.setAttribute("fabricas", fabricas);
                    req.setAttribute("planos", planos);

                    destino = PAGINA_CADASTRO;
                }

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
                case "create" -> registrarPagamento(req);
                case "update" -> atualizarPagamento(req);
                case "delete" -> removerPagamento(req);
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
    private void registrarPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
        String temp = req.getParameter("status").trim();
        boolean status = Boolean.parseBoolean(temp);

        temp = req.getParameter("data_vencimento").trim();
        LocalDate dataVencimento = LocalDate.parse(temp);

        temp = req.getParameter("data_inicio").trim();
        LocalDateTime dataInicio = temp.isBlank() ? null : LocalDate.parse(temp).atStartOfDay();

        LocalDateTime dataPagamento = null;
        double valor;

        // Se o status do pagamento for 'pago', data_pagamento se torna obrigatória
        if (status) {
            temp = req.getParameter("data_pagamento").trim();

            if (temp.isBlank()) {
                throw ExcecaoDeJSP.campoNecessarioFaltante("Data do Pagamento");
            }

            dataPagamento = LocalDate.parse(temp).atStartOfDay();

        }

        temp = req.getParameter("valor").trim();
        valor = Double.parseDouble(temp);

        temp = req.getParameter("metodo_pagamento").trim();
        MetodoPagamento metodoPagamento = MetodoPagamento.deId(Integer.parseInt(temp));

        temp = req.getParameter("fk_fabrica").trim();
        // Verifica se a fábrica foi preenchida
        if (temp.isBlank()) {
            throw ExcecaoDeJSP.campoNecessarioFaltante("fabrica");
        }
        int fkFabrica = Integer.parseInt(temp);

        temp = req.getParameter("fk_plano").trim();
        // Verifica se o plano foi preenchido
        if (temp.isBlank()) {
            throw ExcecaoDeJSP.campoNecessarioFaltante("plano");
        }
        int fkPlano = Integer.parseInt(temp);

        Pagamento pagamento = new Pagamento(null, valor, status, dataVencimento, dataPagamento, dataInicio, metodoPagamento, fkFabrica, fkPlano);

        try (PagamentoDAO dao = new PagamentoDAO()) {
            dao.cadastrar(pagamento);
        }
    }

    // === READ ===
    private List<Pagamento> listarPagamentos(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String campoFiltro = req.getParameter("campo_filtro");
        String campoSequencia = req.getParameter("campo_sequencia");
        String direcaoSequencia = req.getParameter("direcao_sequencia");
        String valorFiltro = null;

        // Verifica se o campo é 'null' para realizar o switch. Se for 'null', o valor do filtro fica como nulo também
        if (campoFiltro != null){

            // Resgata um parâmetro diferente de acordo com o nome do campo de filtragem
            switch (campoFiltro) {
                case "fk_plano" -> valorFiltro = req.getParameter("valor_plano");
                case "fk_fabrica" -> valorFiltro = req.getParameter("valor_fabrica");
                default -> valorFiltro = req.getParameter("valor_filtro");
            }
        }

        try (PagamentoDAO dao = new PagamentoDAO()) {
            Object valorFiltroConvertido = dao.converterValor(campoFiltro, valorFiltro);

            return dao.listar(campoFiltro, valorFiltroConvertido, campoSequencia, direcaoSequencia);
        }
    }

    // === UPDATE ===
    private void atualizarPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
        String temp = req.getParameter("id").trim();
        int id = Integer.parseInt(temp);

        temp = req.getParameter("valor_pago").trim();
        double valorPago = Double.parseDouble(temp);

        temp = req.getParameter("status").trim();
        boolean status = Boolean.parseBoolean(temp);

        temp = req.getParameter("data_vencimento").trim();
        LocalDate dataVencimento = LocalDate.parse(temp);

        LocalDateTime dataPagamento = null;
        // Se o status do pagamento for 'pago', data_pagamento se torna obrigatório
        if (status) {
            temp = req.getParameter("data_pagamento").trim();

            if (temp.isBlank()) {
                throw ExcecaoDeJSP.campoNecessarioFaltante("Data de Pagamento");
            }

            dataPagamento = LocalDate.parse(temp).atStartOfDay();
        }

        temp = req.getParameter("data_inicio").trim();
        LocalDateTime dataInicio = temp.isBlank() ? null : LocalDate.parse(temp).atStartOfDay();

        temp = req.getParameter("metodo_pagamento");
        int fkMetodopag = Integer.parseInt(temp);
        MetodoPagamento metodoPagamento = MetodoPagamento.deId(fkMetodopag);

        temp = req.getParameter("fk_fabrica").trim();
        int fkFabrica = Integer.parseInt(temp);

        temp = req.getParameter("fk_plano").trim();
        int fkPLano = Integer.parseInt(temp);

        Pagamento alterado = new Pagamento(id, valorPago, status, dataVencimento, dataPagamento, dataInicio, metodoPagamento, fkFabrica, fkPLano);

        try (PagamentoDAO dao = new PagamentoDAO()) {
            // Recupera os dados originais do banco de dados
            Pagamento original = dao.getCamposAlteraveis(id);

            dao.atualizar(original, alterado);
        }
    }

    // === DELETE ===
    private void removerPagamento(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String temp = req.getParameter("id").trim();
        int id = Integer.parseInt(temp);

        try (PagamentoDAO dao = new PagamentoDAO()) {
            dao.remover(id);
        }
    }

    // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
    private Pagamento getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String temp = req.getParameter("id").trim();
        int id = Integer.parseInt(temp);

        try (PagamentoDAO dao = new PagamentoDAO()) {
            return dao.pesquisarPorId(id);
        }
    }

    // HashMap das fábricas, onde a chave é o ID e o valor o nome da unidade
    private Map<Integer, String> getMapFabricas() throws SQLException, ClassNotFoundException {
        try (FabricaDAO dao = new FabricaDAO()) {
            return dao.getMapIdNome();
        }
    }

    // HashMap dos planos, onde a chave é o ID e o valor o nome
    private Map<Integer, String> getMapPlanos() throws SQLException, ClassNotFoundException {
        try (PlanoDAO dao = new PlanoDAO()) {
            return dao.getMapIdNome();
        }
    }
}
