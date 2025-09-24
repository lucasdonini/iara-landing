package com.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
O cache do navegador estava entregando a página area-restrita/index.html para o usuário sem passar pelo filtro de autenticação após o logout.
Para impedir essa falha de segurança, tornei a página inicial da área restrita dinâmica (area-restrita/index.jsp) e mapeei um servlet de redirecionamento para a raíz da página, forçando o acionamento do filtro
*/

@WebServlet("/area-restrita/index")
public class RedirectAreaRestritaServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
    rd.forward(req, resp);
  }
}
