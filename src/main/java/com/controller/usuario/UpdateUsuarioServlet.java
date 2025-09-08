package com.controller.usuario;

import com.dao.UsuarioDAO;
import com.dto.AtualizacaoUsuarioDTO;
import com.model.NivelAcesso;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/area-restrita/update-usuario")
public class UpdateUsuarioServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    // Dados da resposta
    AtualizacaoUsuarioDTO camposAlteraveis;
    boolean erro = true;

    try (UsuarioDAO dao  = new UsuarioDAO()) {
      // Recupera as informações atuais do usuário e prepara o display
      camposAlteraveis = dao.getCamposAlteraveis(id);
      req.setAttribute("infosUsuario", camposAlteraveis);
      erro = false;

    }  catch (SQLException e) {
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
      RequestDispatcher rd = req.getRequestDispatcher("usuario/editar.jsp");
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    temp = req.getParameter("fk_fabrica").trim();
    int fkFabrica = Integer.parseInt(temp);

    temp = req.getParameter("nivel_acesso").trim();
    int nivelAcessoInt = Integer.parseInt(temp);
    NivelAcesso nivelAcesso = NivelAcesso.fromInteger(nivelAcessoInt);

    AtualizacaoUsuarioDTO alteracoes = new AtualizacaoUsuarioDTO(
        id,
        req.getParameter("nome"),
        req.getParameter("email"),
        nivelAcesso,
        status,
        fkFabrica
    );

    // Dados da resposta
    String destino = "/erro.html";

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Busca no banco o usuário original
      AtualizacaoUsuarioDTO original = dao.getCamposAlteraveis(id);

      // Atualiza o usuário e setta o destino para mostrar os usuários
      dao.atualizar(original, alteracoes);
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

    // Redireciona para o destino
    resp.sendRedirect(req.getContextPath() + destino);

  }
}
