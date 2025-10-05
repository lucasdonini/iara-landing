package com.exception;

public class ExcecaoDeJSP extends Exception {
  // Construtores
  public ExcecaoDeJSP(String message) {
    super(message);
  }

  public ExcecaoDeJSP(String message, Throwable cause) {
    super(message, cause);
  }

  // Métodos de Fábrica
  public static ExcecaoDeJSP campoUnicoDuplicado(String campo) {
    String msg = "Este %s já está em uso".formatted(campo);
    return new ExcecaoDeJSP(msg);
  }

  public static ExcecaoDeJSP cnpjDuplicado() {
    return campoUnicoDuplicado("CNPJ");
  }

  public static ExcecaoDeJSP emailDuplicado() {
    return campoUnicoDuplicado("Email");
  }

  public static ExcecaoDeJSP nomeDuplicado() {
    return campoUnicoDuplicado("Nome");
  }

  public static ExcecaoDeJSP senhaCurta(int tamanhoMinimo) {
    String msg = "Sua senha deve ter pelo menos %d caracteres.".formatted(tamanhoMinimo);
    return new ExcecaoDeJSP(msg);
  }

  public static ExcecaoDeJSP campoNecessarioFaltante(String campo) {
    String msg = "Por favor, preencha o campo %s.".formatted(campo);
    return new ExcecaoDeJSP(msg);
  }

  public static ExcecaoDeJSP falhaLogin() {
    return new ExcecaoDeJSP("Login falhou. Verifique se Email e Senha.");
  }

  public static ExcecaoDeJSP senhaIncorreta() {
    return new ExcecaoDeJSP("Senha incorreta. Tente novamente.");
  }

  public static ExcecaoDeJSP valorInvalido(String campo) {
    String msg = "O valor informado para %s é inválido.".formatted(campo);
    return new ExcecaoDeJSP(msg);
  }
}
