<%@ page import="java.util.List" %>
<%@ page import="com.model.Plano" %>
<%@ page import="static com.dao.PlanoDAO.camposFiltraveis" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="com.utils.NumerosUtils" %>
<%@ page import="org.postgresql.util.PGInterval" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Plano> planos = (List<Plano>) request.getAttribute("planos");
%>
<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/planos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/crud_geral.css">
</head>

<body>
<main>
    <div id="sidebar">
        <aside>
            <div id="logout">
                <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/pagina_anterior.svg" alt="simbolo de sair">
                <form action="${pageContext.request.contextPath}/login-handler" method="post">
                    <input type="hidden" name="action" value="logout">
                    <button type="submit">Sair</button>
                </form>
            </div>

            <div id="imagem">
                <img id="logo-iara" src="${pageContext.request.contextPath}/assets/imagens gerais/iara_maior.svg" alt="Logo IARA">
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
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/super_adm.svg" alt="icone super adm">
                        <a href="${pageContext.request.contextPath}/area-restrita/superadms">Super ADM</a>
                    </li>
                    <li class="active">
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/planos_azul.svg" alt="icone planos">
                        <a href="${pageContext.request.contextPath}/area-restrita/planos">Planos</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/fabricas.svg" alt="icone fábricas">
                        <a href="${pageContext.request.contextPath}/area-restrita/fabricas">Fábricas</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/pagamento.svg" alt="icone pagamentos">
                        <a href="${pageContext.request.contextPath}/area-restrita/pagamentos">Pagamentos</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/BI.svg" alt="icone BI">
                        <a href="https://iara-area-restrita.vercel.app/home/dashboard">BI</a>
                    </li>
                </ul>
            </nav>
        </aside>
    </div>

    <div id="fundo_tela">

        <div id="topo">
            <div id="local">
                <h2>Gerenciar Planos</h2>
                <p>Gerencie e organize seus Planos</p>
            </div>
            <div id="foto_perfil">
                <img id="perfil" src="${pageContext.request.contextPath}/assets/crud/foto_perfil.svg" alt="foto de perfil">
            </div>
        </div>

        <div id="tela_principal">
            <div id="cabecalho">
                <h1 id="titulo">Lista de Planos</h1>

                <div class="filtro-container">
                    <button class="btn-filtro"
                            onclick="document.querySelector('.filtro-card').classList.toggle('ativo')">
                        Filtro
                    </button>

                    <div class="filtro-card">
                        <form action="${pageContext.request.contextPath}/area-restrita/planos" method="get">
                            <input type="hidden" name="action" value="read">

                            <div class="filtragem">
                                <label>
                                    Campo de Filtragem:
                                    <select id="campoFiltro" name="campo_filtro">
                                        <option value="" selected>Nenhum selecionado</option>
                                        <option value="id" data-type="number">ID</option>
                                        <option value="nome" data-type="text">Nome</option>
                                        <option value="valor" data-type="decimal" placeholder="R$">Valor</option>
                                        <option value="descricao" data-type="text-area">Descrição</option>
                                        <option value="duracao" data-type="duracao">Duração</option>
                                    </select>
                                </label>
                            </div>

                            <div class="filtragem">
                                <label>
                                    Valor Filtrado:
                                    <input type="text" name="valor_filtro">
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

                <form action="${pageContext.request.contextPath}/area-restrita/planos" method="get">
                    <input type="hidden" name="action" value="read">
                    <button id="limpaFiltro" type="submit">Limpar Filtros</button>
                </form>

                <a id="cadastrar"
                   href="${pageContext.request.contextPath}/area-restrita/planos?action=create">Cadastrar</a>
            </div>

            <div id="tabela_usuarios">
                <table border="0">
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Valor</th>
                        <th>Descrição</th>
                        <th>Duração</th>
                    </tr>

                    <% for (Plano plano : planos) { %>
                    <tr>
                        <td>
                            <%= plano.getId() %>
                        </td>
                        <td>
                            <%= plano.getNome() %>
                        </td>
                        <td>
                            <%= NumerosUtils.reais.format(plano.getValor()) %>
                        </td>
                        <td>
                            <%= plano.getDescricao() %>
                        </td>
                        <td>
                            <%
                                PGInterval duracao = plano.getDuracao();
                                int anos = duracao.getYears();
                                int meses = duracao.getMonths();
                                int dias = duracao.getDays();
                            %>

                            <%= anos > 0 ? "%d anos".formatted(anos) : "" %>
                            <%= meses > 0 ? "%d meses".formatted(meses) : "" %>
                            <%= dias > 0 ? "%d dias".formatted(dias) : "" %>

                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/area-restrita/planos" method="get">
                                <input type="hidden" name="id" value="<%= plano.getId() %>">
                                <input type="hidden" name="action" value="update">
                                <button id="editar" type="submit">Editar</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/area-restrita/planos" method="post">
                                <input type="hidden" name="id" value="<%= plano.getId() %>">
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