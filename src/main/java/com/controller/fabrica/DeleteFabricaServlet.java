package com.controller.fabrica;

import com.dao.EnderecoDAO;
import com.dao.FabricaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/area-restrita/delete-fabrica")
public class DeleteFabricaServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Dados da request
    String temp = req.getParameter("id_fabrica");
    int idFabrica = Integer.parseInt(temp);

    temp = req.getParameter("id_endereco");
    int idEndereco = Integer.parseInt(temp);

    // Dados da resposta
    String destino = "/erro.html";

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Deleta o superadm e o endereço
      eDao.remover(idEndereco);
      fDao.remover(idFabrica);

      // Setta o destino para a visualização dos superadms
      destino = "/area-restrita/create-read-fabrica";

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
