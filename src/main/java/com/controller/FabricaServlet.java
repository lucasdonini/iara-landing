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
  // Constantes
  private static final String PAGINA_PRINCIPAL = "jsp/fabricas.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-fabrica.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-fabrica.jsp";
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
      //  Faz a ação correspondente à escolha
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
  private void registrarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição

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

    // Instância do DTO
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

  // === READ ===
  private List<FabricaDTO> listarFabricas(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    //Dados da requisição
    String campoFiltro = req.getParameter("campo_filtro");

    if (campoFiltro!=null && campoFiltro.equals("statusF")){
        campoFiltro = "status";
    }

    String campoSequencia = req.getParameter("campo_sequencia");
    String direcaoSequencia = req.getParameter("direcao_sequencia");
    String valorFiltro = req.getParameter("valor_filtro");

    try (FabricaDAO dao = new FabricaDAO()) {
      if (campoFiltro!=null && !Objects.equals(valorFiltro, "") && !Objects.equals(valorFiltro, null)){
          // Converte o valor
          Object valorFiltroConvertido = dao.converterValor(campoFiltro, valorFiltro);

          // Recupera e retorna os pagamentos cadastrados no banco de dados
          return dao.listar(campoFiltro, valorFiltroConvertido, campoSequencia, direcaoSequencia);

      } else{
          // Transforma o valorFiltro em 'null'
          valorFiltro = null;

          // Recupera e retorna os pagamentos cadastrados no banco de dados
          return dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
      }
    }
  }

  private Pair<Fabrica, Endereco> getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisiçãoo
    String temp = req.getParameter("id").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera os dados originais do banco de dados
      Fabrica f = fDao.pesquisarPorId(idFabrica);
      Endereco e = eDao.pesquisarPorIdFabrica(f.getId());

      // Retorna o pair de Fábrica e Endereço
      return new Pair<>(f, e);
    }
  }

  private Map<Integer, String> getMapPlanos() throws SQLException, ClassNotFoundException {
    try (PlanoDAO dao = new PlanoDAO()) {
      return dao.getMapIdNome();
    }
  }

  // === UPDATE ===
  private void atualizarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
    // Dados da requisição

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

    // Instância do Model
    Endereco endAlterado = new Endereco(null, cep, numero, rua, complemento, idFabrica);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera as informações originais do banco de dados
      Fabrica original = fDao.pesquisarPorId(idFabrica);
      Endereco endOriginal = eDao.pesquisarPorIdFabrica(original.getId());

      // Verifica se as alterações não violam a chave UNIQUE de 'cnpj' em 'fabrica'
      Fabrica teste = fDao.pesquisarPorCnpj(cnpj);
      if (teste != null && teste.getId() != idFabrica) {
        throw ExcecaoDeJSP.cnpjDuplicado();
      }

      // Atualiza a fábrica
      fDao.atualizar(original, alterado);

      // Atualiza o endereço
      eDao.atualizar(endOriginal, endAlterado);
    }
  }

  // === DELETE ===
  private void removerFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da requisição
    String temp = req.getParameter("id_fabrica").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO dao = new FabricaDAO()) {
      // Deleta a fábrica
      dao.remover(idFabrica);
    }
  }
}
