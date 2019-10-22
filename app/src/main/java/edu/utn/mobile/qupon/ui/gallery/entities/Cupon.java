package edu.utn.mobile.qupon.ui.gallery.entities;

public class Cupon {

    public String title;
    public String imgResource;
    public String desc;
    public Double lat;
    public Double lon;


    public Cupon(String title, String desc, String imageResourceURL){
        this.title = title;
        this.desc = desc;
        this.imgResource = imageResourceURL;
    }

}
