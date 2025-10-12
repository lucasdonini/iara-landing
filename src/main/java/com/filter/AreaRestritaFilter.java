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

@WebFilter(filterName = "area-restrita-filter", urlPatterns = "/area-restrita/*")
public class AreaRestritaFilter extends HttpFilter {
  // Constantes
  private static final String PAGINA_LOGIN = "/jsp/login.jsp";

  // Outros Métodos
  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
    HttpSession session = req.getSession(false);

    // Não tem usuário, redireciona para página de login
    if (session != null && session.getAttribute("usuario") instanceof SuperAdmDTO) {
      chain.doFilter(req, resp);

    } else {
      resp.sendRedirect(req.getContextPath() + PAGINA_LOGIN);
    }
  }
}
