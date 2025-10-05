package com.controller;

import com.dao.EnderecoDAO;
import com.dao.FabricaDAO;
import com.dao.PlanoDAO;
import com.dto.CadastroFabricaDTO;
import com.dto.FabricaDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Endereco;
import com.model.Fabrica;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kotlin.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/fabricas")
public class FabricaServlet extends HttpServlet {
  private static final String PAGINA_PRINCIPAL = "WEB-INF/jsp/fabricas.jsp";
  private static final String PAGINA_CADASTRO = "WEB-INF/jsp/cadastro-fabrica.jsp";
  private static final String PAGINA_EDICAO = "WEB-INF/jsp/editar-fabrica.jsp";
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
          List<FabricaDTO> fabricas = listarFabricas(req);
          req.setAttribute("fabricas", fabricas);
          destino = PAGINA_PRINCIPAL;
        }

        case "update" -> {
          Pair<Fabrica, Endereco> p = getInformacoesAlteraveis(req);
          Map<Integer, String> planos = getMapPlanos();

          req.setAttribute("fabrica", p.getFirst());
          req.setAttribute("endereco", p.getSecond());
          req.setAttribute("planos", planos);

          destino = PAGINA_EDICAO;
        }

        case "create" -> {
          Map<Integer, String> planos = getMapPlanos();
          req.setAttribute("planos", planos);

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
      // Faz a ação correspondente à escolha
      switch (action) {
        case "create" -> registrarFabrica(req);
        case "update" -> atualizarFabrica(req);
        case "delete" -> removerFabrica(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    } catch (ExcecaoDeJSP e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

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
    if (erro) {
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);

    } else {
      req.setAttribute("origem", "post");
      doGet(req, resp);
    }
  }

  private void registrarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da request
    // --- Endereço ---
    String temp = req.getParameter("numero").trim();
    int numero = Integer.parseInt(temp);

    String cep = req.getParameter("cep").trim();
    String rua = req.getParameter("logradouro").trim();
    String complemento = req.getParameter("complemento").trim();

    Endereco endereco = new Endereco(null, cep, numero, rua, complemento, null);

    // --- Fábrica ---
    String nome = req.getParameter("nome").trim();
    String cnpj = req.getParameter("cnpj").trim();
    String email = req.getParameter("email").trim();
    String empresa = req.getParameter("empresa").trim();
    String ramo = req.getParameter("ramo").trim();

    temp = req.getParameter("id_plano").trim();
    int idPlano = Integer.parseInt(temp);

    CadastroFabricaDTO credenciais = new CadastroFabricaDTO(nome, cnpj, email, empresa, ramo, idPlano);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Verifica se o cadastro não viola a chave UNIQUE de 'cnpj' em 'fabrica'
      if (fDao.pesquisarPorCnpj(cnpj) != null) {
        throw ExcecaoDeJSP.cnpjDuplicado();
      }

      // Cadastra a fábrica e recupera o id gerado
      int id = fDao.cadastrar(credenciais);

      // Estabelece o relacionamento com endereço
      endereco.setIdFabrica(id);

      // Cadastra o endereço
      eDao.cadastrar(endereco);
    }
  }

  private void atualizarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da request
    // --- Fabrica ---
    String temp = req.getParameter("id_fabrica").trim();
    int idFabrica = Integer.parseInt(temp);

    String nome = req.getParameter("nome").trim();
    String cnpj = req.getParameter("cnpj").trim();

    temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    String email = req.getParameter("email").trim();
    String nomeEmpresa = req.getParameter("nome_empresa").trim();
    String ramo = req.getParameter("ramo").trim();

    temp = req.getParameter("id_plano").trim();
    int idPlano = Integer.parseInt(temp);

    Fabrica alterado = new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);

    // --- Endereço ---
    String cep = req.getParameter("cep").trim();

    temp = req.getParameter("numero").trim();
    int numero = Integer.parseInt(temp);

    String rua = req.getParameter("logradouro").trim();
    String complemento = req.getParameter("complemento").trim();

    Endereco endAlterado = new Endereco(null, cep, numero, rua, complemento, idFabrica);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera as informações originais do banco
      Fabrica original = fDao.pesquisarPorId(idFabrica);
      Endereco endOriginal = eDao.pesquisarPorIdFabrica(original.getId());

      // Verifica se as alterações não violam a chave UNIQUE de cnpj
      Fabrica teste = fDao.pesquisarPorCnpj(cnpj);
      if (teste != null && teste.getId() != idFabrica) {
        throw ExcecaoDeJSP.cnpjDuplicado();
      }

      // Salva as informações no banco
      fDao.atualizar(original, alterado);
      eDao.atualizar(endOriginal, endAlterado);
    }
  }

  private Pair<Fabrica, Endereco> getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera os dados originais da fábrica e retorna
      Fabrica f = fDao.pesquisarPorId(idFabrica);
      Endereco e = eDao.pesquisarPorIdFabrica(f.getId());
      return new Pair<>(f, e);
    }
  }

  private Map<Integer, String> getMapPlanos() throws SQLException, ClassNotFoundException {
    try (PlanoDAO dao = new PlanoDAO()) {
      return dao.getMapIdNome();
    }
  }

  private List<FabricaDTO> listarFabricas(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    try (FabricaDAO dao = new FabricaDAO()) {
      //Dados da requisição
      String campoFiltro = req.getParameter("campoFiltro");
      String temp = req.getParameter("valorFiltro");
      Object valorFiltro = null;

      if (campoFiltro != null && !campoFiltro.isBlank()) {
        valorFiltro = dao.converterValor(campoFiltro, temp);
      }

      String campoSequencia = req.getParameter("campoSequencia");
      String direcaoSequencia = req.getParameter("direcaoSequencia");

      // Recupera os planos do banco
      return dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
    }
  }

  private void removerFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id_fabrica").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO dao = new FabricaDAO()) {
      // Deleta a fábrica
      dao.remover(idFabrica);
    }
  }
}
