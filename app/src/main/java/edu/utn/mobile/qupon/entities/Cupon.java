package edu.utn.mobile.qupon.entities;

import java.io.Serializable;
import java.util.Date;

public class Cupon implements Serializable {

    public String title;
    public String imgResource;
    public String desc;
    public Double lat;
    public Double lon;
    public Date vencimiento;
    public String beaconId;


    public Cupon(String beaconId, String title, String desc, String imageResourceURL, Double lat, Double lon, Date vencimiento){
        this.beaconId = beaconId;
        this.title = title;
        this.desc = desc;
        this.imgResource = imageResourceURL;
        this.lat = lat;
        this.lon = lon;
        this.vencimiento = vencimiento;

    }

}
