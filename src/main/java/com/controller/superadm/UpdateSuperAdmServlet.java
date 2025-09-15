package com.controller.superadm;

import com.dao.SuperAdmDAO;
import com.dto.SuperAdmDTO;
import com.model.SuperAdm;
import com.utils.PasswordUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/area-restrita/update-superadm")
public class UpdateSuperAdmServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String temp = req.getParameter("id");
    int id = Integer.parseInt(temp);

    // Dados da resposta
    SuperAdmDTO adm;
    boolean erro = true;

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Recupera os dados originais para display
      adm = dao.getSuperAdmById(id);
      req.setAttribute("infosAdm", adm);

      // Setta erro como false
      erro = false;

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

    if (erro) {
      resp.sendRedirect(req.getContextPath() + "/erro.html");
    } else {
      RequestDispatcher rd = req.getRequestDispatcher("superadm/editar.jsp");
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Dados da request
    String temp = req.getParameter("id");
    int id = Integer.parseInt(temp);
    String nome = req.getParameter("nome");
    String cargo = req.getParameter("cargo");
    String email = req.getParameter("email");
    String senhaAtual = req.getParameter("senha_atual");
    String novaSenha = req.getParameter("nova_senha");
    SuperAdm alterado = new SuperAdm(id, nome, cargo, email, novaSenha);

    // Dados da resposta
    String destino = "/erro.html";

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Recupera as informações originais do banco
      SuperAdm original = dao.getCamposAlteraveis(id);

      // Se a senha foi alterada e a original estiver incorreta ou a nova for inválida, volta ao formulário
      if (!novaSenha.isBlank() && (!novaSenha.matches(".{8,}") || !PasswordUtils.comparar(original.getSenha(), senhaAtual))) {
        String url = req.getRequestURI() + "?id=" + id;
        resp.sendRedirect(url);
        return;
      }

      // Faz o hash da senha antes de salvar no banco
      String novaSenhaHash = PasswordUtils.hashed(alterado.getSenha());
      alterado.setSenha(novaSenhaHash);

      // Salva as informações no banco
      dao.atualizar(original, alterado);

      // Setta o destino para a página de display
      destino = "/area-restrita/create-read-superadm";

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
