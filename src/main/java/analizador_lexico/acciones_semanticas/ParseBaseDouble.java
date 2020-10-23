package analizador_lexico.acciones_semanticas;

public class ParseBaseDouble extends AccionSemantica {
    /**
     * Verifica si la parte numerica es un numero double y lo asigna a numeroIntD
     * Si es invalido, numeroIntD se vuelve Double.NEGATIVEINFINITY
     */
    public void ejecutar() {
        String doubleString = getString();
        if (getString().equals(".")) setBaseNumDouble(0);
        else setBaseNumDouble(Double.parseDouble(doubleString));

        inicString(); //Reinicia el string temporal.
    }
}
