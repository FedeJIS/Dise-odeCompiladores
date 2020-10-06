package util;

import java.util.ArrayList;
import java.util.List;

public class TablaNotificaciones {
    private static final List<String> errores = new ArrayList<>();
    private static final List<String> warnings = new ArrayList<>();

    public static void agregarError(String error) {
        errores.add(error);
    }

    public static String getErrores(){
        if (errores.isEmpty()) return "Compilacion sin errores";
        StringBuilder builder = new StringBuilder();
        for (String error : errores) builder.append(error).append('\n');
        return builder.toString();
    }

    public static boolean hayErrores() {
        return errores.size() > 0;
    }

    public static void agregarWarning(String warning) {
        warnings.add(warning);
    }

    public static String getWarnings(){
        if (warnings.isEmpty()) return "Compilacion sin warnings";
        StringBuilder builder = new StringBuilder();
        for (String warning : warnings) builder.append(warning).append('\n');
        return builder.toString();
    }
}
