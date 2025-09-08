package com.controller;

import com.dao.LoginDAO;
import com.dto.LoginDTO;
import com.dto.SuperAdmDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

// TODO: Mudar o status de erro quando lançar exceção para um redirecionamento à página de erro

@WebServlet("/login-handler")
public class LoginServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Dados da requisição
    String email = req.getParameter("email");
    String senha = req.getParameter("senha");
    LoginDTO credenciais = new LoginDTO(email, senha);
    HttpSession session = req.getSession();

    // Dados da resposta
    SuperAdmDTO superAdm;
    String destino = "/login.html";

    try (LoginDAO dao = new LoginDAO()) {
      // Tenta fazer login e prepara os dados da resposta de acordo
      superAdm = dao.login(credenciais);

      if (superAdm != null) {
        // Guarda o usuário na sessão
        session.setAttribute("usuario", superAdm);
        destino = "/area-restrita/index.jsp";
      }

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
