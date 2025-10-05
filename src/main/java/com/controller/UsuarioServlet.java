package com.controller;

import com.dao.FabricaDAO;
import com.dao.UsuarioDAO;
import com.dto.AtualizacaoUsuarioDTO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.exception.ExcecaoDePagina;
import com.model.Permissao;
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
import java.util.Map;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
  private static final String PAGINA_PRINCIPAL = "WEB-INF/jsp/usuarios.jsp";
  private static final String PAGINA_CADASTRO = "WEB-INF/jsp/cadastro-usuario.jsp";
  private static final String PAGINA_EDICAO = "WEB-INF/jsp/editar-usuario.jsp";
  private static final String PAGINA_ERRO = "html/erro.html";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Dados da requisição
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
          List<UsuarioDTO> usuarios = getListaUsuarios(req);
          Map<Integer, String> fabricas = getMapFabricas();

          req.setAttribute("usuarios", usuarios);
          req.setAttribute("fabricas", fabricas);
          destino = PAGINA_PRINCIPAL;
        }

        case "update" -> {
          AtualizacaoUsuarioDTO usuario = getInformacoesAlteraveis(req);
          Map<Integer, String> fabricas = getMapFabricas();

          req.setAttribute("usuario", usuario);
          req.setAttribute("fabricas", fabricas);
          destino = PAGINA_EDICAO;
        }

        case "create" -> {
          Map<Integer, String> fabricas = getMapFabricas();

          req.setAttribute("fabricas", fabricas);
          destino = PAGINA_CADASTRO;
        }

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
        case "create" -> registrarUsuario(req);
        case "update" -> atualizarUsuario(req);
        case "delete" -> removerUsuario(req);
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

  private List<UsuarioDTO> getListaUsuarios(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String campoFiltro = req.getParameter("campo_filtro");
    String valorFiltroStr = req.getParameter("valor_filtro");
    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");

    try (UsuarioDAO dao = new UsuarioDAO()) {
      Object valorFiltro = dao.converterValor(campoFiltro, valorFiltroStr);
      // Recupera os usuários do banco e armazena na lista
      return dao.listarUsuarios(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);

    } catch (IllegalArgumentException e) {
      throw ExcecaoDePagina.valorInvalido(UsuarioDAO.camposFiltraveis.get(campoFiltro));
    }
  }

  private Map<Integer, String> getMapFabricas() throws SQLException, ClassNotFoundException {
    try (FabricaDAO dao = new FabricaDAO()) {
      return dao.getMapIdNome();
    }
  }

  private void registrarUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("fk_fabrica").trim();

    if (temp.isBlank()) {
      throw ExcecaoDePagina.campoNecessarioFaltante("fabrica");
    }

    int fkFabrica = Integer.parseInt(temp);

    String senhaOriginal = req.getParameter("senha");
    String hashSenha = SenhaUtils.hashear(temp);

    String email = req.getParameter("email").trim();
    String nome = req.getParameter("nome").trim();

    CadastroUsuarioDTO credenciais = new CadastroUsuarioDTO(nome, email, hashSenha, fkFabrica);

    if (!senhaOriginal.matches(".{8,}")) {
      throw ExcecaoDePagina.senhaCurta(8);
    }

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Verifica se o cadastro viola a constraint UNIQUE de
      if (dao.getUsuarioByEmail(email) != null) {
        throw ExcecaoDePagina.emailDuplicado("super administrador");
      }

      // Cadastra o usuário
      dao.cadastrar(credenciais);
    }
  }

  private AtualizacaoUsuarioDTO getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Recupera as informações atuais do usuário e prepara o display
      return dao.getCamposAlteraveis(id);
    }
  }

  private void atualizarUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    temp = req.getParameter("fk_fabrica").trim();
    int fkFabrica = Integer.parseInt(temp);

    temp = req.getParameter("nivel_acesso").trim();
    int nivelAcessoInt = Integer.parseInt(temp);
    Permissao permissao = Permissao.fromInteger(nivelAcessoInt);

    String email = req.getParameter("email").trim();
    String nome = req.getParameter("nome").trim();

    AtualizacaoUsuarioDTO alteracoes = new AtualizacaoUsuarioDTO(id, nome, email, permissao, status, fkFabrica);

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Busca no banco o usuário original
      AtualizacaoUsuarioDTO original = dao.getCamposAlteraveis(id);

      // Verifica se a atualização não viola a constraint UNIQUE de email
      UsuarioDTO teste = dao.getUsuarioByEmail(email);
      if (teste != null && teste.getId() != id) {
        throw ExcecaoDePagina.emailDuplicado("usuário");
      }

      // Atualiza o usuário
      dao.atualizar(original, alteracoes);
    }
  }

  private void removerUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Remove o usuário
      dao.remover(id);
    }
  }
}
