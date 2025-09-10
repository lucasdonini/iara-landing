package com.controller.planos;

import com.dao.PlanosDAO;
import com.dto.PlanosDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/area-restrita/create-plano")
public class CreateReadPlanosServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //Dados da Request
    String nome = req.getParameter("nome");
    Double valor = Double.parseDouble(req.getParameter("valor"));
    String descricao = req.getParameter("descricao");

    //Instanciando DTO
    PlanosDTO planosDTO = new PlanosDTO(null, nome, valor, descricao);

    try (PlanosDAO planosDAO = new PlanosDAO()) { //DAO instanciado com try-with-resources
      //Cadastra o plano
      planosDAO.cadastrar(planosDTO);

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

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da resposta
    List<PlanosDTO> planosDTOS;
    boolean erro = true;

    try (PlanosDAO dao = new PlanosDAO()) {

      // Recupera os usuários do banco e registra na request
      planosDTOS = dao.listarPlanos();
      req.setAttribute("planos", planosDTOS);

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
      RequestDispatcher rd = req.getRequestDispatcher("planos/index.jsp");
      rd.forward(req, resp);
    }
  }
}
