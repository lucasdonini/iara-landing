<%@ page import="com.model.Fabrica" %>
<%@ page import="com.model.Endereco" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String erro = (String) request.getAttribute("erro");
  Fabrica f = (Fabrica) request.getAttribute("fabrica");
  Endereco e = (Endereco) request.getAttribute("endereco");
  Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
  String complemento = e.getComplemento();
%>
<html lang="pt-BR">

<head>
  <title>Title</title>
  <link rel="stylesheet" href="/styles/editar-fabrica.css">
</head>

<body>
  <a href="${pageContext.request.contextPath}/superadms?action=read" class="btn-sair">Cancelar</a>

  <main class="login-container">
    <img src="/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
      <img src="/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
      <div class="login-box">
        <img src="/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
        <h2>Editar Fábrica - ID: <%= f.getId() %>
        </h2>

        <form action="${pageContext.request.contextPath}/fabricas?action=update" method="post">
          <div class="form-container">
            <div class="form-section">
              <h2>Dados da Fábrica</h2>
              <input type="text" name="nome" value="<%= f.getNomeUnidade() %>" placeholder="Nome">
              <input type="text" name="cnpj" value="<%= f.getCnpj() %>" pattern="\d{14}" title="CNPJ inválido"
                placeholder="CNPJ">
              <input type="email" name="email" value="<%= f.getEmailCorporativo() %>" placeholder="Email para contato">
              <input type="text" name="ramo" value="<%= f.getRamo() %>" placeholder="Ramo">
              <input type="text" name="nome_empresa" value="<%= f.getNomeIndustria() %>" placeholder="Nome da empresa">

              <select name="status">
                <% for (Boolean b : List.of(true, false)) { %>
                  <option value="<%= b.toString() %>" <%=b ? "selected" : "" %>>
                    <%= b ? "Ativa" : "Inativa" %>
                  </option>
                  <% } %>
              </select>

              <select name="id_plano">
                <% for (int idPlano : planos.keySet()) { %>
                  <option value="<%= idPlano %>" <%=idPlano==f.getIdPlano() ? "selected" : "" %>>
                    <%= planos.get(idPlano) %>
                  </option>
                  <% } %>
              </select>
            </div>

            <div class="form-section">
              <h2>Endereço da Fábrica</h2>
              <input type="text" name="cep" value="<%= e.getCep() %>" pattern="\d{8}" title="Insira um CEP válido"
                placeholder="CEP">
              <input type="text" name="logradouro" value="<%= e.getRua() %>" placeholder="Logradouro">
              <input type="number" name="numero" value="<%= e.getNumero() %>" placeholder="n°">
              <input type="text" name="complemento" value="<%= complemento != null ? complemento : "" %>"
                placeholder="Complemento">
            </div>
          </div>

          <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
          <input type="hidden" name="action" value="update">

          <button type="submit" style="display: block">Salvar</button>
        </form>
      </div>
    </div>
  </main>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="/javascript/script.js"></script>

  <% if (erro != null && !erro.isBlank()) { %>
  <p>
    <%= erro %>
  </p>
  <% } %>

</body>

</html>