package edu.utn.mobile.qupon.entities;

import java.io.Serializable;
import java.util.Date;

public class Local implements Serializable {

    public String nombre;
    public Double lat;
    public Double lon;

    public Local(String nombre, Double lat, Double lon){
        this.nombre = nombre;
        this.lat = lat;
        this.lon = lon;
    }

}
