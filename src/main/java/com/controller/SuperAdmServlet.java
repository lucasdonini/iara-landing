package com.controller;

import com.dao.SuperAdmDAO;
import com.dto.SuperAdmDTO;
import com.exception.ExcecaoDeJSP;
import com.model.SuperAdm;
import com.utils.SenhaUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/area-restrita/superadms")
public class SuperAdmServlet extends HttpServlet {
  // Constantes
  private static final String PAGINA_PRINCIPAL = "jsp/superadms.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-superadm.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-superadm.jsp";
  private static final String PAGINA_ERRO = "/html/erro.html";

  // GET e POST
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da requisição
    String action = req.getParameter("action");
    action = (action == null ? "read" : action.trim());

    // Dados da resposta
    boolean erro = true;
    String destino = null;

    try {
      // Faz a ação correspondente à escolha
      switch (action) {
        case "read" -> {
          List<SuperAdmDTO> superAdms = getListaSuperAdms(req);
          req.setAttribute("superAdms", superAdms);
          destino = PAGINA_PRINCIPAL;
        }

        case "update" -> {
          SuperAdmDTO superAdm = getInformacoesAlteraveis(req);
          req.setAttribute("superAdm", superAdm);
          destino = PAGINA_EDICAO;
        }

        case "create" -> destino = PAGINA_CADASTRO;
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    }
    // Se houver alguma exceção, registra no terminal
    catch (SQLException e) {
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    // Redireciona para a página de erro, ou encaminha a requisição e a resposta
    if (erro) {
      resp.sendRedirect(req.getContextPath() + PAGINA_ERRO);

    } else {
      req.getRequestDispatcher(destino).forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da requisição
    String action = req.getParameter("action").trim();

    // Dados da resposta
    String destino = PAGINA_ERRO;

    try {
      switch (action) {
        case "create" -> registrarSuperAdm(req);
        case "update" -> atualizarSuperAdm(req);
        case "delete" -> removerSuperAdm(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      destino = req.getServletPath();

    }
    // Se houver alguma exceção de JSP, aciona o método doGet
    catch (ExcecaoDeJSP e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

    }
    // Se houver alguma exceção grave, registra no terminal
    catch (SQLException e) {
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

  // Outros Métodos

  // === CREATE ===
  private void registrarSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String senhaOriginal = req.getParameter("senha");
    String senhaHash = SenhaUtils.hashear(senhaOriginal);

    String nome = req.getParameter("nome").trim();
    String cargo = req.getParameter("cargo").trim();
    String email = req.getParameter("email").trim();

    // Instância do Model
    SuperAdm credenciais = new SuperAdm(null, nome, cargo, email, senhaHash);

    // Verifica se a senha possui o número mínimo de caracteres
    if (!senhaOriginal.matches(".{8,}")) {
      throw ExcecaoDeJSP.senhaCurta(8);
    }

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Verifica se o super adm não viola a chave UNIQUE de 'email' em 'super_adm'
      SuperAdmDTO teste = dao.pesquisarPorEmail(email);

      if (teste != null) {
        throw ExcecaoDeJSP.emailDuplicado();
      }

      // Cadastra o super adm
      dao.cadastrar(credenciais);
    }
  }

  // === READ ===
  private List<SuperAdmDTO> getListaSuperAdms(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    //Dados da requisição
    String campoFiltro = req.getParameter("campo_filtro");
    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");
    String valorFiltro = req.getParameter("valor_filtro");

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Recupera os super adms cadastrados no banco de dados
      return dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
    }
  }

  private SuperAdmDTO getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Recupera e retorna os dados originais do banco de dados
      return dao.pesquisarPorId(id);
    }
  }

  // === UPDATE ===
  private void atualizarSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String nome = req.getParameter("nome").trim();
    String cargo = req.getParameter("cargo").trim();
    String email = req.getParameter("email").trim();
    String senhaAtual = req.getParameter("senha_atual").trim();
    String novaSenha = req.getParameter("nova_senha").trim();

    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    // Instância do Model
    SuperAdm alterado = new SuperAdm(id, nome, cargo, email, novaSenha);

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Recupera as informações originais do banco
      SuperAdm original = dao.getCamposAlteraveis(id);

      // Verifica se as alterações não violam a chave UNIQUE de 'email' em 'super_adm'
      SuperAdmDTO teste = dao.pesquisarPorEmail(email);
      if (teste != null && teste.getId() != id) {
        throw ExcecaoDeJSP.emailDuplicado();
      }

      if (!novaSenha.isBlank()) {
        // Verifica se a nova senha tem no mínimo 8 caracteres
        if (!novaSenha.matches(".{8,}")) {
          throw ExcecaoDeJSP.senhaCurta(8);
        }

        // Verifica se a senha inserida pelo usuário corresponde com a senha cadastrada no banco de dados
        if (!SenhaUtils.comparar(senhaAtual, original.getSenha())) {
          throw ExcecaoDeJSP.senhaIncorreta();
        }

        // Faz o hash da senha antes de atualizar no banco de dados
        String novaSenhaHash = SenhaUtils.hashear(alterado.getSenha());
        alterado.setSenha(novaSenhaHash);
      }

      // Atualiza o super adm
      dao.atualizar(original, alterado);
    }
  }

  // === DELETE ===
  private void removerSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Deleta o super adm
      dao.remover(id);
    }
  }
}
