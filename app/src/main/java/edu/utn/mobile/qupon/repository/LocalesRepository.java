package edu.utn.mobile.qupon.repository;

import java.util.Arrays;
import java.util.List;

import edu.utn.mobile.qupon.entities.Local;

public class LocalesRepository {

    public List<Local> obtenerLocales() {
        //Mock del API de locales adheridos
        return Arrays.asList(
                new Local("Mc Donald´s", -34.6157437, -58.4244954),
                new Local("Burger King", -34.6203259, -58.3845563),
                new Local("Wendy´s", -34.6102944,-58.3956384)
        );
    }

}
