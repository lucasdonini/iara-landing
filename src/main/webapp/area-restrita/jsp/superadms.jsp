<%@ page import="com.dto.SuperAdmDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="static com.dao.SuperAdmDAO.camposFiltraveis" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<SuperAdmDTO> adms = (List<SuperAdmDTO>) request.getAttribute("superAdms");
%>
<html lang="pt-BR">

<head>
  <title>Landing Teste</title>
  <link rel="stylesheet" href="/styles/superadms.css">
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
            <li class="active">
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
            <li>
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
          <h2>Gerenciar Super Administradores</h2>
          <p>Gerencie e organize seus Super ADMs</p>
        </div>
        <div id="foto_perfil">
          <img id="perfil" src="/assets/crud/foto_perfil.svg" alt="foto de perfil">
        </div>
      </div>

      <div id="tela_principal">
        <div id="cabecalho">
          <h1 id="titulo">Lista de Super ADM</h1>

          <div id="botoes">
            <div class="filtro-container">
              <button class="btn-filtro" onclick="document.querySelector('.filtro-card').classList.toggle('ativo')">
                Filtro
              </button>

              <div class="filtro-card">
                <form action="${pageContext.request.contextPath}/superadms" method="get">
                  <input type="hidden" name="action" value="read">

                  <div class="filtragem">
                    <label>
                      Campo de Filtragem:
                      <select name="campoFiltro">
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
                      Direção de ordenação:
                      <input type="radio" name="direcaoSequencia" value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>"
                        checked>
                      Crescente
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

            <a id="cadastrar" href="${pageContext.request.contextPath}/usuarios?action=create">Cadastrar</a>
          </div>

        </div>

        <div id="tabela_usuarios">
          <table border="0">
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Cargo</th>
              <th>Email</th>
            </tr>

            <% for (SuperAdmDTO adm : adms) { %>
              <tr>
                <td>
                  <%= adm.getId() %>
                </td>
                <td>
                  <%= adm.getNome() %>
                </td>
                <td>
                  <%= adm.getCargo() %>
                </td>
                <td>
                  <%= adm.getEmail() %>
                </td>
                <td>
                  <form action="${pageContext.request.contextPath}/superadms" method="get">
                    <input type="hidden" name="id" value="<%= adm.getId() %>">
                    <input type="hidden" name="action" value="update">
                    <button id="editar" type="submit">Editar</button>
                  </form>
                  <form action="${pageContext.request.contextPath}/superadms" method="post">
                    <input type="hidden" name="id" value="<%= adm.getId() %>">
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