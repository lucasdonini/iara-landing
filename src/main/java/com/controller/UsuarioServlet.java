package com.controller;

import com.dao.FabricaDAO;
import com.dao.UsuarioDAO;
import com.dto.AtualizacaoUsuarioDTO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.exception.ExcecaoDeJSP;
import com.model.TipoAcesso;
import com.utils.SenhaUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@WebServlet("/area-restrita/usuarios")
public class UsuarioServlet extends HttpServlet {
  // Constantes
  private static final String PAGINA_PRINCIPAL = "jsp/usuarios.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-usuario.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-usuario.jsp";
  private static final String PAGINA_ERRO = "/html/erro.html";

  // GET e POST
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
      // Faz a ação correspondente à escolha
      switch (action) {
        case "create" -> registrarUsuario(req);
        case "update" -> atualizarUsuario(req);
        case "delete" -> removerUsuario(req);
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

    // Redireciona para a página de destino
    resp.sendRedirect(req.getContextPath() + destino);
  }

  // Outros Métodos

  // === CREATE ===
  private void registrarUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String email = req.getParameter("email").trim();
    String nome = req.getParameter("nome").trim();
    String senhaOriginal = req.getParameter("senha");
    String hashSenha = SenhaUtils.hashear(senhaOriginal);

    String temp = req.getParameter("id_fabrica").trim();

    if (temp.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("fabrica");
    }

    int idFabrica = Integer.parseInt(temp);

    // Instância do DTO
    CadastroUsuarioDTO credenciais = new CadastroUsuarioDTO(nome, email, hashSenha, idFabrica);

    // Se a senha não tiver no mínimo 8 caracteres, lança uma exceção de JSP
    if (!senhaOriginal.matches(".{8,}")) {
      throw ExcecaoDeJSP.senhaCurta(8);
    }

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Verifica se o cadastro não viola a constraint UNIQUE de 'email' em 'usuario'
      if (dao.pesquisarPorEmail(email) != null) {
        throw ExcecaoDeJSP.emailDuplicado();
      }

      // Cadastra o usuário
      dao.cadastrar(credenciais);
    }
  }

  // === READ ===
  private List<UsuarioDTO> getListaUsuarios(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    //Dados da requisição
    String campoFiltro = req.getParameter("campo_filtro");

    if (campoFiltro!=null && campoFiltro.equals("statusU")){
        campoFiltro = "status";
    }

    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");
    String valorFiltro = req.getParameter("valor_filtro");

    try (UsuarioDAO dao = new UsuarioDAO()) {
      if (campoFiltro!=null && !Objects.equals(valorFiltro, "") && !Objects.equals(valorFiltro, null)){
          // Converte o valor
          Object valorFiltroConvertido = dao.converterValor(campoFiltro, valorFiltro);

          // Recupera e retorna os pagamentos cadastrados no banco de dados
          return dao.listar(campoFiltro, valorFiltroConvertido, campoSequencia, direcaoSequencia);

      } else{
          // Recupera e retorna os pagamentos cadastrados no banco de dados
          return dao.listar(campoFiltro, null, campoSequencia, direcaoSequencia);
      }
    }
  }

  private Map<Integer, String> getMapFabricas() throws SQLException, ClassNotFoundException {
    try (FabricaDAO dao = new FabricaDAO()) {
      return dao.getMapIdNome();
    }
  }

  private AtualizacaoUsuarioDTO getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Recupera e retorna os dados originais do banco de dados
      return dao.getCamposAlteraveis(id);
    }
  }

  // === UPDATE ===
  private void atualizarUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String email = req.getParameter("email").trim();
    String nome = req.getParameter("nome").trim();

    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    temp = req.getParameter("id_fabrica").trim();
    int fkFabrica = Integer.parseInt(temp);

    temp = req.getParameter("nivel_acesso").trim();
    int nivelAcessoInt = Integer.parseInt(temp);
    TipoAcesso tipoAcesso = TipoAcesso.deNivel(nivelAcessoInt);

    // Instância do DTO
    AtualizacaoUsuarioDTO alteracoes = new AtualizacaoUsuarioDTO(id, nome, email, tipoAcesso, status, fkFabrica);

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Busca no banco de dados o usuário original
      AtualizacaoUsuarioDTO original = dao.getCamposAlteraveis(id);

      // Verifica se as alterações não violam a chave UNIQUE de 'email' em 'usuario'
      UsuarioDTO teste = dao.pesquisarPorEmail(email);
      if (teste != null && teste.getId() != id) {
        throw ExcecaoDeJSP.emailDuplicado();
      }

      // Atualiza o usuário
      dao.atualizar(original, alteracoes);
    }
  }

  // === DELETE ===
  private void removerUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (UsuarioDAO dao = new UsuarioDAO()) {
      // Remove o usuário
      dao.remover(id);
    }
  }
}
