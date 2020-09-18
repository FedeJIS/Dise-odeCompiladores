package analizador_lexico;

import java.io.*;
import java.util.ArrayList;

public class FileProcessor {

    /**
     * Lee un txt y guarda cada una de sus lineas en una lista<String>
     * @param path del archivo.
     * @return ArrayList<String> lineas
     */
    public ArrayList<String> getLineas(String path){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> lineas  = new ArrayList<>();

        try {
            // Apertura del fichero
            archivo = new File (path);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null)
                lineas.add(linea);
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // Cierre del fichero
            try{
                if( null != fr )
                    fr.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return lineas;
    }

    /**
     * Escribe un "linea" en el fichero de la ruta "path".
     * @param path ruta del archivo
     * @param linea string a escribir
     * @param existeArchivo si es false, lo crea. Si es true, agrega la linea al final.
     */
    public void escribirArchivo(String path, String linea, boolean existeArchivo){
        FileWriter fichero = null;
        PrintWriter pw;
        try
        {
            //Apertura y escritura del archivo
            fichero = new FileWriter(path,existeArchivo);
            pw = new PrintWriter(fichero);
            pw.println(linea);

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


    /**
     * Renombra un File bajo el nombre "nombreNuevo".
     * @param path del archivo a renombrar
     * @param nombreNuevo nombre sin la extensión
     * @return true si fue correcto, false sino.
     */
    public boolean renombrarFichero(String path, String nombreNuevo){
        File f1= new File(path);
        File f2= new File(nombreNuevo+".txt");
        return f1.renameTo(f2);
    }

    /**
     * Demuestra la existencia o no de un archivo
     * @param path ruta del archivo
     * @return true si existe, false sino.
     */
    public boolean existeArchivo(String path){
        File f = new File(path);
        return (f.exists() && !f.isDirectory());
    }
}
