package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
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

    /**
     * Convierte el objeto Videojuego en un XML
     * @param ruta expecifica la ubicación y el nombre del fichero que vamos a crear
     */
    public void exportarXML(String ruta){
        try{
            JAXBContext context = JAXBContext.newInstance(VideoJuego.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, new File(ruta));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * inserta un videojuego a la lista de videojuegos de la Base de datos
     * @param nuevoJuego datos nuevo juego que vamos a insertar
     * @throws JuegoException contemplamos si el videojuego ya existe en la base de detos
     */
    public void insertarJuego(VideoJuego nuevoJuego) throws JuegoException {
        if (juegos.stream().anyMatch(j -> j.getIdentificador().equals(nuevoJuego.getIdentificador()))){
            throw new JuegoException("Ya existe el juego con ese identificador");
        }
        juegos.add(nuevoJuego);
        numReg++;
    }

    /**
     * Ordena la lista de videojuegos por su identificador
     */
    public void ordenarPorId(){
        juegos.sort(Comparator.comparing(VideoJuego::getIdentificador));
    }

    /**
     * Exportamos y guardamos la lista ordenada para que se refleje
     * @param rutaXML
     */
    public void exportarXMLOrdenado(String rutaXML){
        ordenarPorId();
        exportarXML(rutaXML);
    }

    /**
     * Borra el videojuego por su identificador
     * @param id identificador del videojuego
     * @return devuelve un booleano para saber si el videojuego se pudo eliminar o no
     */
    public boolean borrarPorId(String id){
        boolean eliminado = juegos.removeIf(j -> j.getIdentificador().equals(id));

        if (eliminado){
            numReg--;
            System.out.println("Videojuego con ID " + id + " eliminado");
        }else {
            System.out.println("Videojuego con ID " + id + " no encontrado");
        }
        return eliminado;
    }

    /**
     * Modifica los atributos del videojuego y lo buscamos por su identificador
     * @param id identificador de videojuego
     * @param nuevosDatos los datos nuevos que introduce el usuario
     * @return devuelve un booleano si es true cambia los atributo y si es falso es que el id del videojuego
     * no se ha encontrado
     */
    public boolean modificarPorId(String id, VideoJuego nuevosDatos){
        for (VideoJuego juego : juegos){
            if (juego.getIdentificador().equals(id)){
                juego.setTitulo(nuevosDatos.getTitulo());
                juego.setGenero(nuevosDatos.getGenero());
                juego.setDesarrolladora(nuevosDatos.getDesarrolladora());
                juego.setPegi(nuevosDatos.getPegi());
                juego.setPlataforma(nuevosDatos.getPlataforma());
                juego.setPrecio(nuevosDatos.getPrecio());
                System.out.println("Videojuego con id " + id + " modificado correctamente");
                return true;
            }
        }
        System.out.println("Videojuego con id " + id + " no encontrado");
        return false;
    }

    /**
     * Convierte el objeto videojuego en JSON
     * @param juego es el juego de la lista que quieres convertir
     * @return devuelve un objeto JSON
     */
    public JSONObject convertirJuegoAJSON(VideoJuego juego){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Identificador", juego.getIdentificador());
        jsonObject.put("Título", juego.getTitulo());
        jsonObject.put("Genero", juego.getGenero());
        jsonObject.put("Desarrolladora", juego.getDesarrolladora());
        jsonObject.put("PEGI", juego.getPegi());
        jsonObject.put("Plataforma", juego.getPlataforma());
        jsonObject.put("Precio", juego.getPrecio());

        return jsonObject;
    }

    /**
     * Con el metodo anterior creamos el fichero JSON con los datos del videojuego
     * @param ruta ruta donde queremos crear el fichero nuevo JSON
     * @param juego es el videojuego que queremos guardar sus atributos al JSON
     */
    public void exportarJuegoAJSON(String ruta, VideoJuego juego){
        JSONObject jsonObject = convertirJuegoAJSON(juego);

        try(FileWriter f = new FileWriter(ruta)) {
            f.write(jsonObject.toString(4));
            System.out.println("Exportación a JSON exitosa");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * convierte la lista de videojuegos entera a un fichero JSON apoyandose en el metodo de convertirJuegoJSON
     * @param rutaXML ruta de la base de datos en XML
     * @param rutaJSON ruta donde se va a crear el JSON
     */
    public void convertirBBDDaJSON(String rutaXML, String rutaJSON){
        try{
            JAXBContext context = JAXBContext.newInstance(BaseDeDatos.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            BaseDeDatos bd = (BaseDeDatos) unmarshaller.unmarshal(new File(rutaXML));

            //creo un jsonArray para guardar toda la lista de juegos
            JSONArray jsonArray = new JSONArray();
            for (VideoJuego juego : bd.juegos){
                jsonArray.put(convertirJuegoAJSON(juego));
            }

            //pinta la lista entera en el fichero JSON
            try(FileWriter f = new FileWriter(rutaJSON)){
                f.write(jsonArray.toString(4));
                System.out.println("Conversión de la base de datos a JSON correctamente");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarJuegos(){
        for (VideoJuego juego : juegos){
            System.out.println(juego.toString());
        }
    }
}
