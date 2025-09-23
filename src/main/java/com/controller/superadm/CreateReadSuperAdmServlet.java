package com.controller.superadm;

import com.dao.SuperAdmDAO;
import com.dto.CadastroSuperAdmDTO;
import com.dto.SuperAdmDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
// TODO: criar opções de filtragem para o READ

@WebServlet("/area-restrita/create-read-superadm")
public class CreateReadSuperAdmServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da resposta
    List<SuperAdmDTO> superAdms;
    boolean erro = true;

    try (SuperAdmDAO dao = new SuperAdmDAO()) {

      // Recupera os usuários do banco e registra na request
      superAdms = dao.listarSuperAdms(null, null, null, null);
      req.setAttribute("admins", superAdms);

      // setta erro como false
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

    // Redireciona a request par a página jsp
    if (erro) {
      resp.sendRedirect(req.getContextPath() + "/erro.html");
    } else {
      RequestDispatcher rd = req.getRequestDispatcher("superadm/index.jsp");
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    CadastroSuperAdmDTO credenciais = new CadastroSuperAdmDTO(
        req.getParameter("nome"),
        req.getParameter("cargo"),
        req.getParameter("email"),
        req.getParameter("senha")
    );

    if (!credenciais.getSenha().matches(".{8,}")) {
      resp.sendRedirect(req.getContextPath() + "/area-restrita/superadm/cadastro.html");
      return;
    }

    // Dados da resposta
    String destino = "/erro.html";

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Cadastra o usuário e setta o destino para mostrar os usuários
      dao.cadastrar(credenciais);
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

    // Redireciona para a página de destino
    resp.sendRedirect(req.getContextPath() + destino);
  }
}
