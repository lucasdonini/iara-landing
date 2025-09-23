package com.controller.pagamento;

import com.dao.PagamentoDAO;
import com.dto.PagamentoDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet ("/area-restrita/create-read-pagamento")
public class CreateReadPagamentoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Dados da resposta
        List<PagamentoDTO> pagamentoDTOS;
        boolean erro = true;

        try (PagamentoDAO dao = new PagamentoDAO()) {

            // Recupera os usuários do banco e registra na request
            pagamentoDTOS = dao.listarPagamentos(null, null, null, null);
            req.setAttribute("pagamentos", pagamentoDTOS);

            // setta erro como false
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
            resp.sendRedirect(req.getContextPath() + "/erro.html");
        } else {
            RequestDispatcher rd = req.getRequestDispatcher("pagamento/index.jsp");
            rd.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Definindo formatacao de data
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Dados da Request
        boolean status = Boolean.parseBoolean(req.getParameter("status"));
        LocalDate dataVencimento = LocalDate.parse(req.getParameter("dataVencimento"), dateTimeFormatter);
        LocalDate dataPagamento = LocalDate.parse(req.getParameter("dataPagamento"), dateTimeFormatter);
        String tipoPagamento = req.getParameter("tipoPagamento");
        Integer fkFabrica = Integer.parseInt(req.getParameter("fkFabrica"));
        //Instanciando DTO
        PagamentoDTO pagamentoDTO = new PagamentoDTO(null, null, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);

        try (PagamentoDAO pagamentoDAO = new PagamentoDAO()) { //DAO instanciado com try-with-resources
            //Cadastra o plano
            pagamentoDAO.cadastrar(pagamentoDTO);

            resp.sendRedirect(req.getContextPath() + "/area-restrita/index.jsp");
            //Redirecionando para area restrita

        } catch (SQLException sql) {
            System.err.println("Erro no banco de dados");
            resp.sendRedirect(req.getContextPath() + "/erro.html");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Driver do postgreSQL não encontrado");
            resp.sendRedirect(req.getContextPath() + "/erro.html");
        } catch (Throwable t) {
            System.err.println("Erro não identificado");
            resp.sendRedirect(req.getContextPath() + "/erro.html");
        }
    }
}

