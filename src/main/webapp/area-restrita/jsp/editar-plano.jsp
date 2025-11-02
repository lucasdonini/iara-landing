<%@ page import="com.model.Plano" %>
<%@ page import="org.postgresql.util.PGInterval" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<%
  Plano plano = (Plano) request.getAttribute("plano");
  String erro = (String) request.getAttribute("erro");
  PGInterval duracao = plano.getDuracao();
%>

<head>
  <title>Editar Plano | Área Restrita</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editar-plano.css">
  <link rel="icon"
        href="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/planos" class="btn-sair">Cancelar</a>

<main class="login-container">
  <img src="${pageContext.request.contextPath}/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo"
       class="bg-particles">
  
  <div class="left-side">
    <img src="${pageContext.request.contextPath}/assets/Cadastro/iara-direita_1-removebg-preview%201.png"
         alt="Mascote IARA" class="mascote">
  </div>
  
  <div class="right-side">
    <div class="login-box">
      <img src="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png"
           alt="Logo IARA" class="logo">
      <h2>Editar Plano</h2>
      
      <form action="${pageContext.request.contextPath}/area-restrita/planos" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="<%= plano.getId() %>">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" name="nome" value="<%= plano.getNome() %>" placeholder="Novo nome">
        <label for="valor">Valor:</label>
        <input type="number" id="valor" name="valor" step="any" value="<%= plano.getValor() %>"
               placeholder="Novo valor">
        <label for="descricao">Descrição:</label>
        <input type="text" id="descricao" name="descricao" value="<%= plano.getDescricao() %>"
               placeholder="Nova descricao">
        <label for="anos">Anos:</label>
        <input type="number" id="anos" min="0" placeholder="Anos" value="<%= duracao.getYears() %>" name="anos_duracao">
        <label for="meses">Meses:</label>
        <input type="number" id="meses" min="0" placeholder="Meses" value="<%= duracao.getMonths() %>"
               name="meses_duracao">
        <label for="dias">Dias:</label>
        <input type="number" id="dias" min="0" placeholder="Dias" value="<%= duracao.getDays() %>" name="dias_duracao">
        <button type="submit">Salvar</button>
      </form>
    </div>
  </div>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/script.js"></script>

<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>

</body>

</html>