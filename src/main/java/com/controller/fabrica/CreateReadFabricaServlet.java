package com.controller.fabrica;

import com.dao.EnderecoDAO;
import com.dao.FabricaDAO;
import com.dto.CadastroFabricaDTO;
import com.model.Endereco;
import com.model.Fabrica;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/area-restrita/create-read-fabrica")
public class CreateReadFabricaServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da resposta
    List<Fabrica> fabricas;
    boolean erro = true;

    try (FabricaDAO dao = new FabricaDAO()) {

      // Recupera os usuários do banco e registra na request
      fabricas = dao.listarFabricas();
      req.setAttribute("fabricas", fabricas);

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
      RequestDispatcher rd = req.getRequestDispatcher("fabrica/index.jsp");
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String temp = req.getParameter("numero");
    Endereco endereco = new Endereco(
        null,
        req.getParameter("cep"),
        Integer.parseInt(temp),
        req.getParameter("logradouro"),
        req.getParameter("complemento")
    );
    CadastroFabricaDTO credenciais = new CadastroFabricaDTO(
        req.getParameter("nome"),
        req.getParameter("cnpj"),
        req.getParameter("email"),
        req.getParameter("empresa"),
        req.getParameter("ramo"),
        0
    );

    // Dados da resposta
    String destino = "/erro.html";

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Cadastra o endereço e recupera o id gerado
      int id = eDao.cadastrar(endereco);

      // setta o id do endereco nas credenciais da fabrica
      credenciais.setFkEndereco(id);

      // Cadastra o usuário e setta o destino para mostrar os usuários
      fDao.cadastrar(credenciais);
      destino = "/area-restrita/create-read-fabrica";

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
