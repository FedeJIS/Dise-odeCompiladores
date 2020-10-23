package util;

public class CodigoFuente {
    private final String cFuente;

    private int posicionActual = 0;

    public CodigoFuente(String cFuente) {
        this.cFuente = cFuente;
    }

    public void avanzar() {
        if (eof()) return; //Evita seguir avanzando cuando se esta en el eof.
        posicionActual++;
    }

    public void retroceder() {
        if (posicionActual == 0) return; //Evita seguir retrocediendo cuando se esta en el principio del archivo.
        posicionActual--;
    }

    public boolean eof() {
        return posicionActual == cFuente.length();
    }

    public char simboloActual() {
        return cFuente.charAt(posicionActual);
    }

    public char simboloAnterior(){
        if (posicionActual == 0) throw new IllegalStateException("No se puede leer el simbolo anterior.");
        return cFuente.charAt(posicionActual-1);
    }
}
