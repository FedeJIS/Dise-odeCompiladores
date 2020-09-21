package analizador_lexico;

public class TestGeneral {
    public static void main(String[] args) {

        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.escribirArchivo("./archivo.txt","hola",false);
        if( fileProcessor.existeArchivo("./archivo.txt"))
            System.out.println("si");
    }
}
