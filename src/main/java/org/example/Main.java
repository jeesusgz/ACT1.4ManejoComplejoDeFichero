package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    //definimos los campos y su longitud de caracteres de la base de datos
    public static void main(String[] args) {
        Map<String, Integer> campos = new HashMap<>();
        campos.put("identificador", 10);
        campos.put("titulo", 50);
        campos.put("genero", 20);
        campos.put("desarrolladora", 50);
        campos.put("pegi", 3);
        campos.put("plataforma", 20);
        campos.put("precio", 20);

        //cremamos la Base de datos
        BaseDeDatos bd = new BaseDeDatos("videojuegos.csv", campos, "identificador");
        Scanner sc = new Scanner(System.in);

        int opcion;

        do {
            System.out.println("1. Exportar Base de datos a XML");
            System.out.println("2. Inserta un videojuego");
            System.out.println("3. Ordena la lista por el campo ID");
            System.out.println("4. Modifica los atributos de un videojuego representado por su ID");
            System.out.println("5. Exporta la información de un videojuego a un fichero JSON");
            System.out.println("6. Exporta todos los videojuegos a un fichero JSON");
            System.out.println("7. Mostrar todos los videojuegos");
            System.out.println("8. Salir");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion){
                case 1:
                    bd.exportarXML("videoJuego.xml");
                    System.out.println("base de datos exportada correctamente a XML");
                case 2:

            }
        }while (opcion != 8);
        }
    }
}