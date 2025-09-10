package com.filter;

import com.dto.SuperAdmDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// @WebFilter(filterName = "area-restrita-filter", urlPatterns = "/area-restrita/*")
public class AreaRestritaFilter extends HttpFilter {
  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
    // Procura o usuário logado na sessão
    HttpSession session = req.getSession(false);
    String target = req.getRequestURI();
    Object usuario = (session != null ? session.getAttribute("usuario") : null);

    if (target.endsWith("/logout")) {
      chain.doFilter(req, resp);
      return;
    }

    // Não tem usuário, redireciona para página de login
    if (usuario instanceof SuperAdmDTO) {
      chain.doFilter(req, resp);
    } else {
      resp.sendRedirect(req.getContextPath() + "/login.html");
    }
  }
}
