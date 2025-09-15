package com.controller.usuario;

import com.dao.FabricaDAO;
import com.dao.UsuarioDAO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.utils.PasswordUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: criar opções de filtragem para o READ

@WebServlet("/area-restrita/create-read-usuario")
public class CreateReadUsuarioServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da resposta
    Map<UsuarioDTO, String> map = new HashMap<>();
    List<UsuarioDTO> usuarios;
    Map<Integer, String> infosFabricas;
    boolean erro = true;

    try (UsuarioDAO uDao = new UsuarioDAO(); FabricaDAO fDao = new FabricaDAO()) {

      // Recupera os usuários do banco e registra na request
      usuarios = uDao.listarUsuarios();

      // Recupera as informações da fábrica
      infosFabricas = fDao.getNomes();

      // Combina os usuários com suas devidas fábricas
      for (UsuarioDTO u : usuarios) {
        int id = u.getFkFabrica();
        String nome = infosFabricas.get(id);
        String visualizacao = "%s - ID: %d".formatted(nome, id);
        map.put(u, visualizacao);
      }

      // Salva as informações na request
      req.setAttribute("usuarios", map);

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
    String senhaOriginal = req.getParameter("senha");
    String hashSenha = PasswordUtils.hashed(temp);
    CadastroUsuarioDTO credenciais = new CadastroUsuarioDTO(
        req.getParameter("nome"),
        req.getParameter("email"),
        hashSenha,
        fkFabrica
    );

    if (!senhaOriginal.matches(".{8,}")) {
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
