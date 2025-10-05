package com.controller;

import com.dao.LoginDAO;
import com.dto.LoginDTO;
import com.dto.SuperAdmDTO;
import com.exception.ExcecaoDePagina;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login-handler")
public class LoginServlet extends HttpServlet {
  private static final String PAGINA_ERRO = "html/erro.html";
  private static final String AREA_RESTRITA = "WEB-INF/jsp/area-restrita.jsp";
  private static final String PAGINA_LOGIN = "WEB-INF/jsp/login.jsp";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    RequestDispatcher rd = req.getRequestDispatcher(PAGINA_LOGIN);
    rd.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da requisição
    String action = req.getParameter("action").trim();
    HttpSession session = req.getSession();

    // Dados da resposta
    boolean redirect = true;
    String destino = PAGINA_ERRO;

    try {
      switch (action) {
        case "login" -> {
          SuperAdmDTO usuario = login(req);
          session.setAttribute("usuario", usuario);
          destino = AREA_RESTRITA;
          redirect = false;
        }

        case "logout" -> {
          logout(req);
          doGet(req, resp);
          return;
        }

        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

    } catch (ExcecaoDePagina e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

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

    if (redirect) {
      resp.sendRedirect(req.getContextPath() + '/' + destino);

    } else {
      RequestDispatcher rd = req.getRequestDispatcher(destino);
      rd.forward(req, resp);
    }
  }

  private SuperAdmDTO login(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String email = req.getParameter("email").trim();
    String senha = req.getParameter("senha").trim();
    LoginDTO credenciais = new LoginDTO(email, senha);

    try (LoginDAO dao = new LoginDAO()) {
      // Tenta fazer login e recuperar o usuário
      SuperAdmDTO usuario = dao.login(credenciais);

      // Verifica se o login deu certo
      if (usuario == null) {
        throw ExcecaoDePagina.falhaAutenticacao();
      }

      return usuario;
    }
  }

  private void logout(HttpServletRequest req) {
    // Dados da request
    HttpSession session = req.getSession(false);

    if (session != null) {
      session.removeAttribute("usuario");
    }
  }
}
