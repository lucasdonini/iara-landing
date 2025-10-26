package com.controller;

import com.dao.EnderecoDAO;
import com.dao.FabricaDAO;
import com.dao.PlanoDAO;
import com.dto.CadastroFabricaDTO;
import com.dto.FabricaDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Endereco;
import com.model.Fabrica;
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
import java.util.Objects;

@WebServlet("/area-restrita/fabricas")
public class FabricaServlet extends HttpServlet {

  private static final String PAGINA_PRINCIPAL = "jsp/fabricas.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-fabrica.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-fabrica.jsp";
  private static final String PAGINA_ERRO = "/html/erro.html";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    String action = req.getParameter("action");
    action = (action == null ? "read" : action.trim());

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

        default -> throw new IllegalArgumentException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    } catch (SQLException e) {
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
      resp.sendRedirect(req.getContextPath() + PAGINA_ERRO);

    } else {
      req.getRequestDispatcher(destino).forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

    String action = req.getParameter("action").trim();
    String destino = PAGINA_ERRO;

    try {
      switch (action) {
        case "create" -> registrarFabrica(req);
        case "update" -> atualizarFabrica(req);
        case "delete" -> removerFabrica(req);
        default -> throw new IllegalArgumentException("valor inválido para o parâmetro 'action': " + action);
      }

      destino = req.getServletPath();

    }
    // Se houver alguma exceção de JSP, aciona o método doGet
    catch (ExcecaoDeJSP e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

    } catch (SQLException e) {
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    resp.sendRedirect(req.getContextPath() + destino);
  }

  // === CREATE ===
  private void registrarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {

    // --- Endereço ---
    String temp = req.getParameter("numero").trim();
    int numero = Integer.parseInt(temp);

    String cep = req.getParameter("cep").trim();
    String rua = req.getParameter("logradouro").trim();
    String complemento = req.getParameter("complemento").trim();
    String bairro = req.getParameter("bairro").trim();
    String cidade = req.getParameter("cidade").trim();
    String estado = req.getParameter("estado").trim();


    Endereco endereco = new Endereco(null, cep, numero, rua, complemento, null, bairro, cidade, estado);

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

      int id = fDao.cadastrar(credenciais);

      // Estabelece o relacionamento com endereço
      endereco.setIdFabrica(id);

      eDao.cadastrar(endereco);
    }
  }

  // === READ ===
  private List<FabricaDTO> listarFabricas(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    String campoFiltro = req.getParameter("campo_filtro");

    if (Objects.equals(campoFiltro, "statusF")) {
      campoFiltro = "status";
    }

    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");
    String valorFiltro = req.getParameter("valor_filtro");

    try (FabricaDAO dao = new FabricaDAO()) {
      Object valorFiltroConvertido = dao.converterValor(campoFiltro, valorFiltro);

      return dao.listar(campoFiltro, valorFiltroConvertido, campoSequencia, direcaoSequencia);
    }
  }

  // === UPDATE ===
  private void atualizarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {

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

    // Instância do Model
    Fabrica alterado = new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);

    // --- Endereço ---
    String cep = req.getParameter("cep").trim();

    temp = req.getParameter("numero").trim();
    int numero = Integer.parseInt(temp);

    String rua = req.getParameter("logradouro").trim();
    String complemento = req.getParameter("complemento").trim();
    String bairro = req.getParameter("bairro").trim();
    String cidade = req.getParameter("cidade").trim();
    String estado = req.getParameter("estado").trim();

    Endereco endAlterado = new Endereco(null, cep, numero, rua, complemento, idFabrica, bairro, cidade, estado);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera as informações originais do banco de dados
      Fabrica original = fDao.pesquisarPorId(idFabrica);
      Endereco endOriginal = eDao.pesquisarPorIdFabrica(original.getId());

      // Verifica se as alterações não violam a chave UNIQUE de 'cnpj' em 'fabrica'
      Fabrica teste = fDao.pesquisarPorCnpj(cnpj);
      if (teste != null && teste.getId() != idFabrica) {
        throw ExcecaoDeJSP.cnpjDuplicado();
      }

      fDao.atualizar(original, alterado);
      eDao.atualizar(endOriginal, endAlterado);
    }
  }

  // === DELETE ===
  private void removerFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {

    String temp = req.getParameter("id_fabrica").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO dao = new FabricaDAO()) {
      dao.remover(idFabrica);
    }
  }

  // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
  private Pair<Fabrica, Endereco> getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {

    String temp = req.getParameter("id").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera os dados originais do banco de dados
      Fabrica f = fDao.pesquisarPorId(idFabrica);
      Endereco e = eDao.pesquisarPorIdFabrica(f.getId());

      return new Pair<>(f, e);
    }
  }

  // HashMap dos planos, onde a chave é o ID e o valor o nome
  private Map<Integer, String> getMapPlanos() throws SQLException, ClassNotFoundException {
    try (PlanoDAO dao = new PlanoDAO()) {
      return dao.getMapIdNome();
    }
  }
}
