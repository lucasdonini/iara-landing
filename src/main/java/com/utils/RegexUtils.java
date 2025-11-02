package com.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
  public static final String REGEX_EMAIL = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
  public static final String REGEX_CNPJ = "\\d{14}";
  public static final String REGEX_CPF = "\\d{11}";
  public static final String REGEX_CEP = "\\d{8}";

  private static boolean validarCampo(String campo, String regex) {
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(campo);
    return m.matches();
  }

  public static boolean validarEmail(String email) {
    return validarCampo(email, REGEX_EMAIL);
  }

  public static boolean validarCnpj(String cnpj) {
    return validarCampo(cnpj, REGEX_CNPJ);
  }

  public static boolean validarCpf(String cpf) {
    return validarCampo(cpf, REGEX_CPF);
  }

  public static boolean validarCep(String cep) {
    return validarCampo(cep, REGEX_CEP);
  }
}
