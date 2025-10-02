package com.controller;

import com.dto.SuperAdmDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/area-restrita")
public class AreaRestritaServlet extends HttpServlet {
  private static final String PAGINA_PRINCIPAL = "WEB-INF/jsp/area-restrita.jsp";
  private static final String PAGINA_LOGIN = "WEB-INF/jsp/login.jsp";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da requisição
    HttpSession session = req.getSession(false);

    // Dados da resposta
    String destino = PAGINA_PRINCIPAL;

    if (session == null || !(session.getAttribute("usuario") instanceof SuperAdmDTO)) {
      destino = PAGINA_LOGIN;
    }

    RequestDispatcher rd = req.getRequestDispatcher(destino);
    rd.forward(req, resp);
  }
}
