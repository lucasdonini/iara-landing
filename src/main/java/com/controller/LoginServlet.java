package com.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.dao.LoginDAO;
import com.dto.LoginDTO;
import com.dto.SuperAdmDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Dados da requisição
    String email = req.getParameter("email");
    String senha = req.getParameter("senha");
    LoginDTO credenciais = new LoginDTO(email, senha);
    HttpSession session = req.getSession();

    // Dados da resposta
    SuperAdmDTO superAdm;
    int status = SC_INTERNAL_SERVER_ERROR;

    try (LoginDAO dao = new LoginDAO()) {
      // Tenta fazer login e prepara os dados da resposta de acordo
      superAdm = dao.login(credenciais);

      if (superAdm != null) {
        status = SC_OK;
        session.setAttribute("usuario", superAdm);

      } else {
        status = SC_UNAUTHORIZED;
      }

    } catch (SQLException e) {
      // Se houver alguma exceção, registra no terminal e retorna com status 500
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace();

      status = SC_INTERNAL_SERVER_ERROR;

    } catch (Exception e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace();

      status = SC_INTERNAL_SERVER_ERROR;

    } finally {
      // Redireciona a request par a página jsp
      RequestDispatcher dp = req.getRequestDispatcher("area-restrita.jsp");
      resp.setStatus(status);
      dp.forward(req, resp);
    }
  }
}
