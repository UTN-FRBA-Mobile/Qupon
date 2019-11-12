package edu.utn.mobile.qupon.ui.gallery.entities;

import java.io.Serializable;

public class Cupon implements Serializable {

    public String title;
    public String imgResource;
    public String desc;
    public Double lat;
    public Double lon;
    public String beaconId = "1111";


    public Cupon(String title, String desc, String imageResourceURL, Double lat, Double lon){
        this.title = title;
        this.desc = desc;
        this.imgResource = imageResourceURL;
        this.lat = lat;
        this.lon = lon;

    }

}
