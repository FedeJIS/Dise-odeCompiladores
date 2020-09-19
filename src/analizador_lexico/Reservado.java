package analizador_lexico;

import java.util.ArrayList;
import java.util.Hashtable;

public class Reservado {
    private ArrayList<String> palabrasReserv;
    public Reservado(){
        palabrasReserv=new ArrayList<String>();
    }

    public boolean esReservada(String palabra){
        for (String palabraAux : palabrasReserv){
            if (palabra.equals(palabraAux))
                return true;
        }
        return false;
    }

    public boolean agregar(String palabra){
        if (!esReservada(palabra)) {
            palabrasReserv.add(palabra);
            return true;
        }
        return false;
    }

}
