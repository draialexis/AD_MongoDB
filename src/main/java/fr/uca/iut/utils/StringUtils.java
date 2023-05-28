package fr.uca.iut.utils;

public class StringUtils {
    public static boolean isBlankStringOrNull(String string) {
        return string == null || string.isBlank();
    }
}
