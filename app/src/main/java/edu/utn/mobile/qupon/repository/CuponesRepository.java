package edu.utn.mobile.qupon.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.utn.mobile.qupon.entities.Cupon;

public class CuponesRepository {

    public List<Cupon> obtenerTodos() {
        //Mock del storage local
        ArrayList<Cupon> dataSet = new ArrayList<>();
        dataSet.add(new Cupon("0x000000014567", "McDonalds", "50% de descuento en Sundaes", "https://picsum.photos/300",
                -34.6010156, -58.4287612, new Date()));
        dataSet.add(new Cupon("0x000000014598", "Burguer King", "2x1 en Triple Kings", "https://picsum.photos/301",
                -34.6009797, -58.4287612, new Date()));
        dataSet.add(new Cupon("0x000000014671", "Wendys", "Llevate una ensalada de regalo", "https://picsum.photos/302",
                -34.6008861, -58.4550259, new Date()));
        return dataSet;
    }

    public Cupon obtenerPorBeaconId(String beaconId) {
        //Mock del llamado a API
        switch (beaconId) {
            case "0x000000014567":
                return new Cupon("0x000000014567", "McDonalds", "50% de descuento en Sundaes", "https://picsum.photos/300",
                        -34.6010156, -58.4287612, new Date());
            case "0x000000014598":
                return new Cupon("0x000000014598", "Burguer King", "2x1 en Triple Kings", "https://picsum.photos/301",
                        -34.6009797, -58.4287612, new Date());
            case "0x000000014671":
                return new Cupon("0x000000014671", "Wendys", "Llevate una ensalada de regalo", "https://picsum.photos/302",
                        -34.6008861, -58.4550259, new Date());
            default:
                return null;
        }
    }
}
