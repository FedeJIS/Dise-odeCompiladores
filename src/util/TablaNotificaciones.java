package util;

import java.util.ArrayList;
import java.util.List;

public class TablaNotificaciones {
    private static final List<String> errores = new ArrayList<>();
    private static final List<String> warnings = new ArrayList<>();

    public static void agregarError(String error) {
        errores.add(error);
    }

    public static boolean hayErrores() {
        return errores.size() > 0;
    }

    public static void agregarWarning(String warning) {
        warnings.add(warning);
    }
}
