package org.example;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDeDatos {
    private final String nombreFich;
    private final Map<String, Integer> campos;
    private final String campoClave;

    private int longReg;
    private long numReg;
    private long numRegMarcadosBorrado;
    private List<VideoJuego> juegos;

    public BaseDeDatos(String nombreFich, Map<String, Integer> campos, String campoClave) {
        this.nombreFich = nombreFich;
        this.campos = campos;
        this.campoClave = campoClave;
        this.numReg = 0;
        this.numRegMarcadosBorrado = 0;
        this.juegos = new ArrayList<>();

        for (Map.Entry<String, Integer> campo : campos.entrySet()){
            this.longReg += campo.getValue();
        }

        cargarRegistro();
    }

    /**
     * Cargamos los datos del csv para ahora crear un XML y volcarlo alli
     */
    public void cargarRegistro(){
        try(BufferedReader br = new BufferedReader(new FileReader(nombreFich))){
            br.readLine();
            String linea;

            //separamos los datos y lo capturamos
            while ((linea = br.readLine()) != null){
                String[] datos = linea.split(",");
                VideoJuego juego = new VideoJuego();
                juego.setIdentificador(datos[0]);
                juego.setTitulo(datos[1]);
                juego.setGenero(datos[2]);
                juego.setDesarrolladora(datos[3]);
                juego.setPegi(Integer.parseInt(datos[4]));
                juego.setPlataforma(datos[5]);
                juego.setPrecio(Double.parseDouble(datos[6].replace("€","")));

                //añadimos los videojuegos a la lista juegos y vamos creando los registros de la base de datos
                juegos.add(juego);
                numReg++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el fichero");
        } catch (IOException e) {
            System.err.println("Ha habido un fallo con el fichero");
        }
    }

    public void volcarDatosXML(String ruta){
        try{
            JAXBContext context = JAXBContext.newInstance(BaseDeDatos.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, new File(ruta));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
