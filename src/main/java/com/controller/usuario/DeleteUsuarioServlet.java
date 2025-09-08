package com.controller.usuario;

import com.dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/area-restrita/delete-usuario")
public class DeleteUsuarioServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Dados da request
    String temp = req.getParameter("id");
    int id = Integer.parseInt(temp);

    // Dados da resposta
    String destino = "/erro.html";

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Remove o usuário e setta o destino para a página de visualização de usuário
      dao.remover(id);
      destino = "/area-restrita/create-read-usuario";

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
