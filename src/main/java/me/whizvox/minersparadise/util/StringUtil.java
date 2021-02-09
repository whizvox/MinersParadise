package me.whizvox.minersparadise.util;

import java.text.DecimalFormat;
import java.util.TreeMap;

public class StringUtil {

  private static final TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>();
  static {
    ROMAN_NUMERALS.put(1000, "M");
    ROMAN_NUMERALS.put(900, "CM");
    ROMAN_NUMERALS.put(500, "D");
    ROMAN_NUMERALS.put(400, "CD");
    ROMAN_NUMERALS.put(100, "C");
    ROMAN_NUMERALS.put(90, "XC");
    ROMAN_NUMERALS.put(50, "L");
    ROMAN_NUMERALS.put(40, "XL");
    ROMAN_NUMERALS.put(10, "X");
    ROMAN_NUMERALS.put(9, "IX");
    ROMAN_NUMERALS.put(5, "V");
    ROMAN_NUMERALS.put(4, "IV");
    ROMAN_NUMERALS.put(1, "I");
  }

  // shamelessly stolen from https://stackoverflow.com/a/19759564
  public static String formatAsRomanNumeral(int n) {
    if (n < 1) {
      return Integer.toString(n);
    }
    int l = ROMAN_NUMERALS.floorKey(n);
    if (n == l) {
      return ROMAN_NUMERALS.get(n);
    }
    return ROMAN_NUMERALS.get(l) + formatAsRomanNumeral(n - l);
  }

  private static final String[] COMMON_SUFFIXES = {
      "k", "M", "B", "T", "P", "E"
  };
  public static String formatAsCompactNotation(int n) {
    if (n < 1000) {
      return Integer.toString(n);
    }
    float f = (float) n;
    int mag = -1;
    do {
      f /= 1000.0F;
      mag++;
    } while (f > 1000 && mag < COMMON_SUFFIXES.length);
    return (int) f + "." + (int) (f * 10) % 10 + COMMON_SUFFIXES[mag];
  }

  private static final DecimalFormat CLEAN_FORMAT = new DecimalFormat("#.##");
  public static String formatAsCleanFloat(double n) {
    return CLEAN_FORMAT.format(n);
  }

}
