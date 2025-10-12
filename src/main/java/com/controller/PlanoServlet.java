package com.controller;

import com.dao.PlanoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Plano;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/area-restrita/planos")
public class PlanoServlet extends HttpServlet {
  // Constantes
  private static final String PAGINA_PRINCIPAL = "jsp/planos.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-plano.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-plano.jsp";
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
          List<Plano> planos = listaPlanos(req);
          req.setAttribute("planos", planos);
          destino = PAGINA_PRINCIPAL;
        }

        case "update" -> {
          Plano plano = getInformacoesAlteraveis(req);
          req.setAttribute("plano", plano);
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
      // Faz a ação correspondente à escolha
      switch (action) {
        case "create" -> registrarPlano(req);
        case "update" -> atualizarPlano(req);
        case "delete" -> removerPlano(req);
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
  private void registrarPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String nome = req.getParameter("nome").trim();
    String descricao = req.getParameter("descricao").trim();

    String temp = req.getParameter("valor").trim();

    if (temp.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("valor");
    }

    double valor = Double.parseDouble(temp);

    // Instância do Model
    Plano plano = new Plano(null, nome, valor, descricao);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Verifica se o novo plano não viola a chave UNIQUE
      if (dao.pesquisarPorNome(nome) != null) {
        throw ExcecaoDeJSP.nomeDuplicado();
      }

      // Cadastra o plano
      dao.cadastrar(plano);
    }
  }

  // === READ ===
  private List<Plano> listaPlanos(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    //Dados da requisição
    String campoFiltro = req.getParameter("campo_filtro");
    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");
    String valorFiltro = req.getParameter("valor_filtro");

    try (PlanoDAO dao = new PlanoDAO()) {
      // Recupera os planos cadastrados no banco de dados
      return dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
    }
  }

  private Plano getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Recupera e retorna os dados originais do banco de dados
      return dao.pesquisarPorId(id);
    }
  }

  // === UPDATE ===
  private void atualizarPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição
    String nome = req.getParameter("nome").trim();
    String descricao = req.getParameter("descricao").trim();

    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    temp = req.getParameter("valor").trim();
    if (temp.isBlank()) {
      throw ExcecaoDeJSP.campoNecessarioFaltante("valor");
    }
    double valor = Double.parseDouble(temp);

    // Instância do Model
    Plano alterado = new Plano(id, nome, valor, descricao);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Recupera os dados originais do banco de dados
      Plano original = dao.getCamposAlteraveis(id);

      // Verifica se as alterações não violam a chave UNIQUE de 'nome' em 'plano'
      Plano teste = dao.pesquisarPorNome(nome);
      if (teste != null && id != teste.getId()) {
        throw ExcecaoDeJSP.nomeDuplicado();
      }

      // Atualiza o plano
      dao.atualizar(original, alterado);
    }
  }

  // === DELETE ===
  private void removerPlano(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id").trim();
    int id = Integer.parseInt(temp);

    try (PlanoDAO dao = new PlanoDAO()) {
      // Deleta o plano
      dao.remover(id);
    }
  }
}
