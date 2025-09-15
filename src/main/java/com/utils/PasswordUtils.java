package com.utils;

import org.mindrot.jbcrypt.BCrypt;

// Classe utilitária para lidar com a criptografia da senha antes de salvar no banco
public class PasswordUtils {
  public static String hashed(String senha) {
    // Usa o esquema bcrypt da OpenBSD para criptografar a senha usando o salt gerado
    return BCrypt.hashpw(senha, BCrypt.gensalt(15));
  }

  public static boolean comparar(String senha, String hash) {
    // Usa o método de comparação da biblioteca para conferir se uma senha em texto puro é igual á que passou pelo hash
    return BCrypt.checkpw(senha, hash);
  }

  // Documentação da biblioteca: https://www.mindrot.org/files/jBCrypt/jBCrypt-0.1-doc/BCrypt.html
}
