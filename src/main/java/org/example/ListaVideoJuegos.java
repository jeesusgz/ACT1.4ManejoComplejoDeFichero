package org.example;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "juegos")
public class ListaVideoJuegos {
    private List<VideoJuego> videoJuegos;

    public ListaVideoJuegos() {
    }

    public ListaVideoJuegos(List<VideoJuego> juegos) {
        this.videoJuegos = juegos;
    }

    @XmlElement(name = "videojuego")
    public List<VideoJuego> getVideoJuegos() {
        return videoJuegos;
    }

    public void setVideoJuegos(List<VideoJuego> videoJuegos) {
        this.videoJuegos = videoJuegos;
    }
}
