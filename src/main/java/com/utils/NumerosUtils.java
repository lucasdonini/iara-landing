package com.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumerosUtils {
  public static final NumberFormat reais = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
}
