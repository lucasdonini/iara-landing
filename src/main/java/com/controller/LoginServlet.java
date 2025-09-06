package com.controller;

import com.dao.LoginDAO;
import com.dto.LoginDTO;
import com.dto.SuperAdmDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/login-handler")
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
    String destino = "/login.jsp";

    try {
      LoginDAO dao = new LoginDAO();

      // Tenta fazer login e prepara os dados da resposta de acordo
      superAdm = dao.login(credenciais);

      if (superAdm != null) {
        status = SC_OK;
        session.setAttribute("usuario", superAdm);
        destino = "/area-restrita.jsp";

      } else {
        status = SC_UNAUTHORIZED;
      }

      dao.close();

    } catch (SQLException e) {
      // Se houver alguma exceção, registra no terminal e retorna com status 500
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

      status = SC_INTERNAL_SERVER_ERROR;

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

      status = SC_INTERNAL_SERVER_ERROR;

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);

      status = SC_INTERNAL_SERVER_ERROR;

    } finally {
      // Redireciona a request par a página jsp
      RequestDispatcher dp = req.getRequestDispatcher(req.getContextPath() + '/' + destino);
      resp.setStatus(status);
      dp.forward(req, resp);
    }
  }
}
