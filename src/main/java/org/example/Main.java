package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    //definimos los campos y su longitud de caracteres de la base de datos
    public static void main(String[] args) {
        Map<String, Integer> campos = new HashMap<>();
        campos.put("identificador", 5);
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

            switch (opcion) {
                case 1:
                    bd.exportarXML("videoJuego.xml");
                    System.out.println("base de datos exportada correctamente a XML");
                    break;
                case 2:
                    VideoJuego juegoInsertado = new VideoJuego();
                    System.out.println("Introduzca el identificador: ");
                    juegoInsertado.setIdentificador(sc.nextLine());
                    System.out.println("Introduzca el titulo: ");
                    juegoInsertado.setTitulo(sc.nextLine());
                    System.out.println("Introduzca el genero: ");
                    juegoInsertado.setGenero(sc.nextLine());
                    System.out.println("Introduzca el desarrolladora: ");
                    juegoInsertado.setDesarrolladora(sc.nextLine());
                    System.out.println("Introduzca el PEGI: ");
                    juegoInsertado.setPegi(sc.nextLine());
                    System.out.println("Introduzca el precio: ");
                    juegoInsertado.setPrecio(Double.parseDouble(sc.nextLine()));
                    System.out.println("Introduzca la plataforma: ");
                    juegoInsertado.setPlataforma(sc.nextLine());

                    try{
                        bd.insertarJuego(juegoInsertado);
                        bd.exportarXML("videoJuego.xml");
                        System.out.println("VideoJuego insertado correctamente");
                    } catch (JuegoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    bd.ordenarPorId();
                    bd.exportarXMLOrdenado("videoJuego.xml");
                    System.out.println("Fichero XML ordenado correctamente");
                    break;
                case 4:
                    System.out.println("Introduce el ID del videojuego a modificar: ");
                    String id = sc.nextLine();
                    VideoJuego datos = new VideoJuego();
                    System.out.println("Introduce el nuevo titulo: ");
                    datos.setTitulo(sc.nextLine());
                    System.out.println("Introduce el nuevo genero: ");
                    datos.setGenero(sc.nextLine());
                    System.out.println("Introduce el nuevo desarrolladora: ");
                    datos.setDesarrolladora(sc.nextLine());
                    System.out.println("Introduce el nuevo PEGI: ");
                    datos.setPegi(sc.nextLine());
                    System.out.println("Introduce el nuevo precio: ");
                    datos.setPrecio(Integer.parseInt(sc.nextLine()));
                    System.out.println("Introduce la nueva plataforma: ");
                    datos.setPlataforma(sc.nextLine());

                    if (bd.modificarPorId(id, datos)){
                        System.out.println("VideoJuego modificado correctamente");
                    }else {
                        System.out.println("VideoJuego no existe");
                    }
                    break;
                case 5:
                    System.out.println("Introduce el ID del videojuego a exportar a JSON: ");
                    String idExportado = sc.nextLine();
                    VideoJuego juegoExportado = null;

                    for (VideoJuego juego : bd.getJuegos()){
                        if (juego.getIdentificador().equals(idExportado)){
                            juegoExportado = juego;
                            break;
                        }
                    }
                    if (juegoExportado != null){
                        bd.exportarJuegoAJSON("videoJuego.json", juegoExportado);
                    }else {
                        System.out.println("VideoJuego no existe");
                    }
                    break;
                case 6:
                    bd.convertirBBDDaJSON("videojuego.xml","videojuego.json");
                    System.out.println("Base de datos convertida a JSON correctamente");
                    break;
                case 7:
                    bd.mostrarJuegos();
                    break;
                case 8:
                    System.out.println("Saliendo del programa");
                    break;
                default:
                    System.out.println("Introduce una opción válida");
            }
        } while (opcion != 8);
        sc.close();
    }
}