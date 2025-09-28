package com.controller;

import com.dao.PagamentoDAO;
import com.dao.PlanoDAO;
import com.dto.PagamentoDTO;
import com.exception.ExcecaoDePagina;
import com.model.Pagamento;
import com.model.Plano;
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

@WebServlet("/area-restrita/pagamentos")
public class PagamentoServlet extends HttpServlet {
    private static final String PAGINA_PRINCIPAL = "jsp/pagamentos.jsp";
    private static final String PAGINA_CADASTRO = "jsp/cadastro-pagamento.jsp";
    private static final String PAGINA_EDICAO = "jsp/editar-pagamento.jsp";
    private static final String PAGINA_ERRO = "html/erro.html";
    private static final String ESSE_ENDPOINT = "area-restrita/pagamentos";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Dados da requisição
        String action = req.getParameter("action").trim();

        // Dados da resposta
        boolean erro = true;
        String destino = null;

        try {
            switch (action) {
                case "read" -> {
                    List<Pagamento> pagamentos = listarPagamentos(req);
                    req.setAttribute("pagamentos", pagamentos);
                    destino = PAGINA_PRINCIPAL;
                }

                case "update" -> {
                    PagamentoDTO pagamento = getInformacoesAlteraveis(req);
                    req.setAttribute("infosPagamento", pagamento);
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
        String destino = PAGINA_ERRO;

        try {
            switch (action) {
                case "create" -> registrarPagamento(req);
                case "update" -> atualizarPagamento(req);
                case "delete" -> removerPagamento(req);
                case "read" -> {
                    List<Pagamento> pagamentos = listarPagamentos(req);
                    req.setAttribute("pagamentos", pagamentos);
                }
                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            destino = ESSE_ENDPOINT + "?action=read";

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
        resp.sendRedirect(req.getContextPath() + '/' + destino);
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

    private PagamentoDTO getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
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
        if (temp.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("status");
        }
        boolean status = Boolean.parseBoolean(temp);

        String temp2 = req.getParameter("dataVencimento").trim();
        if (temp2.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("dataVencimento");
        }
        LocalDate dataVencimento = LocalDate.parse(temp2);

        String temp3 = req.getParameter("dataPagamento").trim();
        if (temp2.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("dataPagamento");
        }
        LocalDate dataPagamento = LocalDate.parse(temp3);

        String tipoPagamento = req.getParameter("tipoPagamento").trim();

        String temp4 = req.getParameter("fkFabrica").trim();
        if (temp4.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("fkFabrica");
        }
        int fkFabrica = Integer.parseInt(temp4);
        PagamentoDTO pagamento = new PagamentoDTO(null, null, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);

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

        String temp2 = req.getParameter("valorPago").trim();
        if (temp2.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("valorPago");
        }
        double valorPago = Double.parseDouble(temp2);

        String temp3 = req.getParameter("status").trim();
        if (temp3.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("status");
        }
        boolean status = Boolean.parseBoolean(temp3);

        String temp4 = req.getParameter("dataVencimento").trim();
        if (temp4.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("dataVencimento");
        }
        LocalDate dataVencimento = LocalDate.parse(temp4);

        String temp5 = req.getParameter("dataPagamento").trim();
        if (temp5.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("dataPagamento");
        }
        LocalDate dataPagamento = LocalDate.parse(temp5);

        String tipoPagamento = req.getParameter("tipoPagamento").trim();

        String temp6 = req.getParameter("fkFabrica").trim();
        if (temp6.isBlank()) {
            throw ExcecaoDePagina.campoNecessarioFaltante("fkFabrica");
        }
        int fkFabrica = Integer.parseInt(temp6);

        Pagamento alterado = new Pagamento(id, valorPago, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);

        try (PagamentoDAO dao = new PagamentoDAO()) {
            // Recupera as informações originais do banco
            Pagamento original = dao.getCamposAlteraveis(id);

            // Salva as informações no banco
            dao.atualizar(original, alterado);
        }
    }
}
