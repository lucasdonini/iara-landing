package com.controller.usuario;

import com.dao.UsuarioDAO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
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

@WebServlet("/area-restrita/create-read-usuario")
public class CreateReadUsuarioServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da resposta
    List<UsuarioDTO> usuarios;
    boolean erro = true;

    try (UsuarioDAO dao = new UsuarioDAO()) {

      // Recupera os usuários do banco e registra na request
      usuarios = dao.listarUsuarios();
      req.setAttribute("usuarios", usuarios);

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
      RequestDispatcher rd = req.getRequestDispatcher("usuario/index.jsp");
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Dados da requisição
    String temp = req.getParameter("fk_fabrica").trim();
    int fkFabrica = Integer.parseInt(temp);
    CadastroUsuarioDTO credenciais = new CadastroUsuarioDTO(
        req.getParameter("nome"),
        req.getParameter("email"),
        req.getParameter("senha"),
        fkFabrica
    );

    if (!credenciais.getSenha().matches(".{8,}")) {
      resp.sendRedirect(req.getContextPath() + "/usuario/cadastro.html");
    }

    // Dados da resposta
    String destino = "/erro.html";

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Cadastra o usuário e setta o destino para mostrar os usuários
      dao.cadastrar(credenciais);
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

    // Redireciona para a página de destino
    resp.sendRedirect(req.getContextPath() + destino);
  }
}
