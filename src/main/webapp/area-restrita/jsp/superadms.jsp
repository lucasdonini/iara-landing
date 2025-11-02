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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/superadms.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/crud_geral.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body>
<main>
  <div id="sidebar">
    <aside>
      <div id="logout">
        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/pagina_anterior.svg"
             alt="simbolo de sair">
        <form action="${pageContext.request.contextPath}/login-handler" method="post">
          <input type="hidden" name="action" value="logout">
          <button type="submit">Sair</button>
        </form>
      </div>

      <div id="imagem">
        <img id="logo-iara" src="${pageContext.request.contextPath}/assets/imagens gerais/iara_maior.svg"
             alt="Logo IARA">
      </div>

      <nav>
        <ul>
          <li>
            <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/home.svg" alt="icone home">
            <a href="${pageContext.request.contextPath}/area-restrita/index.jsp">Página inicial</a>
          </li>
          <li>
            <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/usuario.svg" alt="icone usuarios">
            <a href="${pageContext.request.contextPath}/area-restrita/usuarios">Usuários</a>
          </li>
          <li class="active">
            <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/super_adm_azul.svg"
                 alt="icone super adm">
            <a href="${pageContext.request.contextPath}/area-restrita/superadms">Super ADM</a>
          </li>
          <li>
            <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/planos.svg" alt="icone planos">
            <a href="${pageContext.request.contextPath}/area-restrita/planos">Planos</a>
          </li>
          <li>
            <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/fabricas.svg" alt="icone fábricas">
            <a href="${pageContext.request.contextPath}/area-restrita/fabricas">Fábricas</a>
          </li>
          <li>
            <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/pagamento.svg"
                 alt="icone pagamentos">
            <a href="${pageContext.request.contextPath}/area-restrita/pagamentos">Pagamentos</a>
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
    </div>

    <div id="tela_principal">
      <div id="cabecalho">
        <h1 id="titulo">Lista de Usuários</h1>

        <div class="filtro-container">
          <button class="btn-filtro"
                  onclick="document.querySelector('.filtro-card').classList.toggle('ativo')">
            Filtro
          </button>

          <div class="filtro-card">
            <form action="${pageContext.request.contextPath}/area-restrita/superadms" method="get">
                            <input type="hidden" name="action" value="read">

                            <div class="filtragem">
                                <label>
                                    Campo de Filtragem:
                                    <select id="campoFiltro" name="campo_filtro" onchange="tipoCampoPlanoAdm()">
                    <option value="" selected>Nenhum selecionado</option>
                    <option value="nome" data-type="text">Nome</option>
                                        <option value="cargo" data-type="text">Cargo</option>
                                        <option value="email" data-type="email">Email</option>
                                    </select>
                                </label>
                            </div>

                            <div class="filtragem">
                                <label>
                                    <label for="valorFiltro">Valor Filtrado:</label>
                                    <input type="text"
                                           id="valorFiltro"
                                           name="valor_filtro"
                                    >
                                </label>
                            </div>

                            <div class="filtragem">
                                <label>
                                    Ordenar por:
                                    <select name="campo_sequencia">
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
                                    Direção de ordenação
                                    <input type="radio" name="direcao_sequencia"
                                           value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>"
                                           checked> Crescente
                                    <input type="radio" name="direcao_sequencia"
                                           value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>">
                                    Decrescente
                                </label>
                            </div>

                            <input type="submit" value="Filtrar" id="filtrar">
                        </form>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/area-restrita/superadms" method="get">
          <input type="hidden" name="action" value="read">
          <button id="limpaFiltro" type="submit">Limpar Filtros</button>
        </form>

        <a id="cadastrar" href="${pageContext.request.contextPath}/area-restrita/superadms?action=create">Cadastrar</a>
      </div>

      <div id="tabela_usuarios">
        <table border="0">
          <tr>
            <th>Nome</th>
            <th>Cargo</th>
            <th>Email</th>
          </tr>

          <% for (SuperAdmDTO adm : adms) { %>
          <tr>
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
              <form action="${pageContext.request.contextPath}/area-restrita/superadms" method="get">
                <input type="hidden" name="id" value="<%= adm.getId() %>">
                <input type="hidden" name="action" value="update">
                <button id="editar" type="submit">Editar</button>
              </form>
              <form action="${pageContext.request.contextPath}/area-restrita/superadms" method="post"
                    onsubmit="confirmarDelete(event)">
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
<script src="${pageContext.request.contextPath}/javascript/infoTrader.js"></script>
</body>

</html>