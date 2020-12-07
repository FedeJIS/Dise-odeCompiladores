import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar(
                "UINT a,b,c,d;" + '\n' +
                "PROC lala(UINT e,UINT f,UINT g) NI = 3_ui{" + '\n' +
                    "OUT(\"lala\");" + '\n' +
                "};" + '\n' +
                "lala(3_ui,c,d);"
                ,true,true);
    }
}
