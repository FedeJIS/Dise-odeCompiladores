package util;

import java.io.*;

public class FileProcessor {

    /**
     * Lee un txt y guarda cada una de sus lineas en una lista<String>
     *
     * @param path del archivo.
     * @return ArrayList<String> lineas
     */
    public static String getLineasFuente(String path) {
        File archivo;
        FileReader fr = null;
        BufferedReader br;
        StringBuilder builderFuente = new StringBuilder();

        try {
            // Apertura del fichero
            archivo = new File(path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null)
                builderFuente.append(linea).append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cierre del fichero
            try {
                if (null != fr)
                    fr.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return builderFuente.substring(0,builderFuente.length()-1); //Saca el '\n' hecho luego de leer la ultima linea.
    }

    /**
     * Escribe un "linea" en el fichero de la ruta "path".
     *
     * @param path          ruta del archivo
     * @param linea         string a escribir
     * @param existeArchivo si es false, lo crea. Si es true, agrega la linea al final.
     */
    public static void escribirArchivo(String path, String linea, boolean existeArchivo) throws FileNotFoundException{
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            //Apertura y escritura del archivo
            fichero = new FileWriter(path, existeArchivo);
            pw = new PrintWriter(fichero);
            pw.println(linea);

        } catch (FileNotFoundException fileNotFoundException) {
            throw fileNotFoundException;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //Cierre del archivo
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


}
