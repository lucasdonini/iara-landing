package com.controller;

import com.dao.SuperAdmDAO;
import com.dto.CadastroSuperAdmDTO;
import com.dto.SuperAdmDTO;
import com.exception.ExcecaoDePagina;
import com.model.SuperAdm;
import com.utils.SenhaUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/superadms")
public class SuperAdmServlet extends HttpServlet {
  private static final String PAGINA_PRINCIPAL = "WEB-INF/jsp/superadms.jsp";
  private static final String PAGINA_CADASTRO = "WEB-INF/jsp/cadastro-superadm.jsp";
  private static final String PAGINA_EDICAO = "WEB-INF/jsp/editar-superadm.jsp";
  private static final String PAGINA_ERRO = "html/erro.html";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String action = req.getParameter("action").trim();
    Object origem = req.getAttribute("origem");

    if ("post".equals(origem)) {
      action = "read";
    }

    // Dados da resposta
    boolean erro = true;
    String destino = null;

    try {
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
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);
    } else {
      RequestDispatcher rd = req.getRequestDispatcher(destino);
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String action = req.getParameter("action").trim();

    // Dados da resposta
    boolean erro = true;

    try {
      switch (action) {
        case "create" -> registrarSuperAdm(req);
        case "update" -> atualizarSuperAdm(req);
        case "delete" -> removerSuperAdm(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    } catch (ExcecaoDePagina e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

    } catch (SQLException e) {
      // Se houver alguma exceção grave, registra no terminal
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
    if (erro) {
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);

    } else {
      req.setAttribute("origem", "post");
      doGet(req, resp);
    }
  }

  private List<SuperAdmDTO> getListaSuperAdms(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String campoFiltro = req.getParameter("campo_filtro");
    String valorFiltroStr = req.getParameter("valor_filtro");
    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      Object valorFiltro = dao.converterValor(campoFiltro, valorFiltroStr);

      // Recupera os usuários do banco
      return dao.listarSuperAdms(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
    }
  }

  private SuperAdmDTO getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Recupera os dados originais para display
      return dao.getSuperAdmById(id);
    }
  }

  private void registrarSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String senhaOriginal = req.getParameter("senha");
    String senhaHash = SenhaUtils.hashear(senhaOriginal);

    String nome = req.getParameter("nome").trim();
    String cargo = req.getParameter("cargo").trim();
    String email = req.getParameter("email").trim();

    CadastroSuperAdmDTO credenciais = new CadastroSuperAdmDTO(nome, cargo, email, senhaHash);

    if (!senhaOriginal.matches(".{8,}")) {
      throw ExcecaoDePagina.senhaCurta(8);
    }

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Verifica se o usuário não viola a chave UNIQUE de email
      SuperAdmDTO teste = dao.getSuperAdmByEmail(email);
      if (teste != null) {
        throw ExcecaoDePagina.emailDuplicado("super administrador");
      }

      // Cadastra o usuário e setta o destino para mostrar os usuários
      dao.cadastrar(credenciais);
    }
  }

  private void removerSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Deleta o superadm
      dao.remover(id);
    }
  }

  private void atualizarSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    String nome = req.getParameter("nome").trim();
    String cargo = req.getParameter("cargo").trim();
    String email = req.getParameter("email").trim();
    String senhaAtual = req.getParameter("senha_atual").trim();
    String novaSenha = req.getParameter("nova_senha").trim();

    SuperAdm alterado = new SuperAdm(id, nome, cargo, email, novaSenha);

    try (SuperAdmDAO dao = new SuperAdmDAO()) {
      // Recupera as informações originais do banco
      SuperAdm original = dao.getCamposAlteraveis(id);

      // Verifica se o email alterado não viola a chave UNIQUE
      SuperAdmDTO teste = dao.getSuperAdmByEmail(email);
      if (teste != null && teste.getId() != id) {
        throw ExcecaoDePagina.emailDuplicado("super administrador");
      }

      // Se a senha foi alterada e a original estiver incorreta ou a nova for inválida, volta ao formulário
      if (!novaSenha.isBlank()) {
        // Verifica a validade das alterações
        if (!novaSenha.matches(".{8,}")) {
          throw ExcecaoDePagina.senhaCurta(8);
        }

        if (!SenhaUtils.comparar(senhaAtual, original.getSenha())) {
          throw ExcecaoDePagina.falhaAutenticacao();
        }

        // Faz o hash da senha antes de salvar no banco
        String novaSenhaHash = SenhaUtils.hashear(alterado.getSenha());
        alterado.setSenha(novaSenhaHash);
      }

      // Salva as informações no banco
      dao.atualizar(original, alterado);
    }
  }
}
