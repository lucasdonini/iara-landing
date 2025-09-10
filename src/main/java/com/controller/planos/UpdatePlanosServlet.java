package com.controller.planos;

import com.dao.PlanosDAO;
import com.dto.PlanosDTO;
import com.model.Planos;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/area-restrita/update-plano")
public class UpdatePlanosServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    int id = Integer.parseInt(req.getParameter("id"));

    // Dados da resposta
    PlanosDTO plano;
    boolean erro = true;

    try (PlanosDAO dao = new PlanosDAO()) {
      // Recupera os dados originais para display
      plano = dao.getPlanoById(id);
      req.setAttribute("infosPlano", plano);

      // Setta erro como false
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

    if (erro) {
      resp.sendRedirect(req.getContextPath() + "/erro.html");
    } else {
      RequestDispatcher rd = req.getRequestDispatcher("planos/editar.jsp");
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Dados da request
    int id = Integer.parseInt(req.getParameter("id"));
    String nome = req.getParameter("nome");
    Double valor = Double.parseDouble(req.getParameter("valor"));
    String descricao = req.getParameter("descricao");
    Planos alterado = new Planos(id, nome, valor, descricao);

    // Dados da resposta
    String destino = "/erro.html";

    try (PlanosDAO dao = new PlanosDAO()) {
      // Recupera as informações originais do banco
      Planos original = dao.getCamposAlteraveis(id);

      // Salva as informações no banco
      dao.atualizar(original, alterado);

      // Setta o destino para a página de display
      destino = "/area-restrita/create-plano";

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

    resp.sendRedirect(req.getContextPath() + destino);
  }
}
