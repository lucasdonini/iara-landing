package com.filter;

import java.io.IOException;

import com.dto.SuperAdmDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName="area-restrita-filter", urlPatterns="/area-restrita.jsp")
public class AreaRestritaFilter extends HttpFilter {
  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
    // Procura o usuário logado na sessão
    HttpSession session = req.getSession();
    Object usuario = session.getAttribute("usuario");

    // Não tem usuário, redireciona para página de login
    if (usuario instanceof SuperAdmDTO) {
      chain.doFilter(req, resp);
    } else {
      resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
  }
}
