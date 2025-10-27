<%@ page import="com.model.Pagamento" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.dao.PagamentoDAO.camposFiltraveis" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="com.model.MetodoPagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<Pagamento> pagamentos = (List<Pagamento>) request.getAttribute("pagamentos");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
%>

<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <link rel="stylesheet" href="/styles/planos.css">
  <link rel="stylesheet" href="/styles/crud_geral.css">
</head>

<body>
  <main>
    <div id="sidebar">
      <aside>
        <div id="logout">
          <img class="imagem" src="/assets/crud/pagina_anterior.svg" alt="simbolo de sair">
          <form action="${pageContext.request.contextPath}/login-handler" method="post">
            <input type="hidden" name="action" value="logout">
            <button type="submit">Sair</button>
          </form>
        </div>

        <div id="imagem">
          <img id="logo-iara" src="/assets/imagens gerais/iara_maior.svg" alt="Logo IARA">
        </div>

        <nav>
          <ul>
            <li>
              <img class="imagem" src="/assets/crud/home.svg" alt="icone home">
              <a href="${pageContext.request.contextPath}/index.html">Página inicial</a>
            </li>
            <li>
              <img class="imagem" src="/assets/crud/usuario.svg" alt="icone usuarios">
              <a href="${pageContext.request.contextPath}/usuarios?action=read">Usuários</a>
            </li>
            <li>
              <img class="imagem" src="/assets/crud/super_adm.svg" alt="icone super adm">
              <a href="${pageContext.request.contextPath}/superadms?action=read">Super ADM</a>
            </li>
            <li>
              <img class="imagem" src="/assets/crud/planos.svg" alt="icone planos">
              <a href="${pageContext.request.contextPath}/planos?action=read">Planos</a>
            </li>
            <li>
              <img class="imagem" src="/assets/crud/fabricas.svg" alt="icone fábricas">
              <a href="${pageContext.request.contextPath}/fabricas?action=read">Fábricas</a>
            </li>
            <li class="active">
              <img class="imagem" src="/assets/crud/pagamento.svg" alt="icone pagamentos">
              <a href="${pageContext.request.contextPath}/pagamentos?action=read">Pagamentos</a>
            </li>
            <li>
              <img class="imagem" src="/assets/crud/BI.svg" alt="icone BI">
              <a href="#">BI</a>
            </li>
          </ul>
        </nav>
      </aside>
    </div>

    <div id="fundo_tela">

      <div id="topo">
        <div id="local">
          <h2>Gerenciar Pagamentos</h2>
          <p>Gerencie e organize seus Pagamentos</p>
        </div>
        <div id="foto_perfil">
          <img id="perfil" src="/assets/crud/foto_perfil.svg" alt="foto de perfil">
        </div>
      </div>

      <div id="tela_principal">
        <div id="cabecalho">
          <h1 id="titulo">Lista de Pagamentos</h1>

          <div class="filtro-container">
            <button class="btn-filtro" onclick="document.querySelector('.filtro-card').classList.toggle('ativo')">
              Filtro
            </button>

            <div class="filtro-card">
              <form action="${pageContext.request.contextPath}/planos" method="get">
                <input type="hidden" name="action" value="read">

                <div class="filtragem">
                  <label>
                    Campo de Filtragem:
                    <select name="campoFiltro">
                      <option value="" selected>Nenhum selecionado</option>

                      <% for (String chave : camposFiltraveis.keySet()) { %>
                        <option value="<%= chave %>">
                          <%= camposFiltraveis.get(chave) %>
                        </option>
                        <% } %>
                    </select>
                  </label>
                </div>

                <div class="filtragem">
                  <label>
                    Valor Filtrado:
                    <input type="text" name="valorFiltro">
                  </label>
                </div>

                <div class="filtragem">
                  <label>
                    Ordenar por:
                    <select name="campoSequencia">
                      <option value="" selected>Nenhum selecionado</option>

                      <% for (String chave : camposFiltraveis.keySet()) { %>
                        <option value="<%= camposFiltraveis.get(chave) %>">
                          <%= chave %>
                        </option>
                        <% } %>
                    </select>
                  </label>
                </div>

                <div class="filtragem">
                  <label>
                    Direção de ordenação
                    <input type="radio" name="direcaoSequencia" value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>"
                      checked> Crescente
                    <input type="radio" name="direcaoSequencia" value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>">
                    Decrescente
                  </label>
                </div>

                <input type="submit" value="Filtrar" id="filtrar">
              </form>
            </div>
          </div>

          <form action="${pageContext.request.contextPath}/fabricas" method="get">
            <input type="hidden" name="action" value="read">
            <button id="limpaFiltro" type="submit">Limpar Filtros</button>
          </form>

          <a id="cadastrar" href="${pageContext.request.contextPath}/pagamentos?action=create">Cadastrar</a>
        </div>

        <div id="tabela_usuarios">
          <table border="0">
            <tr>
              <th>ID</th>
              <th>Valor Pago</th>
              <th>Status</th>
              <th>Data de Vencimento</th>
              <th>Data de Pgto.</th>
              <th>Tipo de Pgto.</th>
              <th>Fábrica</th>
            </tr>
            <%
              for (Pagamento pagamento : pagamentos) {
                LocalDateTime dtPagto = pagamento.getDataPagamento();
            %>
            <tr>
              <td>
                <%= pagamento.getId() %>
                 101
              </td>
              <td>
                <%= pagamento.getValor() %>
                 R$ 200.000.000
              </td>
              <td>
                <%= pagamento.getStatus() ? "Pago" : "Pendente" %>
                 Ativo
              </td>
              <td>
                <%= pagamento.getDataVencimento() %>
                 23/10/2017
              </td>
              <td>
                <%= dtPagto == null ? "Pagamento Pendente" : dtPagto %>
                 06/10/2018
              </td>
              <td>
                <%= MetodoPagamento.deNivel(pagamento.getMetodoPagamento().getNivel()) %>
                 Cartão de Crédito
              </td>
              <td>
                <%= fabricas.get(pagamento.getFkFabrica()) %>
                 Fábrica Alphax
              </td>
              <td>
                <form action="${pageContext.request.contextPath}/pagamentos" method="get">
                  <input type="hidden" name="id" value="<%= pagamento.getId() %>">
                  <input type="hidden" name="action" value="update">
                  <button id="editar" type="submit">Editar</button>
                </form>
                <form action="${pageContext.request.contextPath}/pagamentos" method="post">
                  <input type="hidden" name="id" value="<%= pagamento.getId() %>">
                  <input type="hidden" name="action" value="delete">
                  <button id="deletar" type="submit">Deletar</button>
                </form>
              </td>
            </tr>
            <% } %>
          </table>
        </div>

      </div>
    </div>

  </main>
</body>

</html>