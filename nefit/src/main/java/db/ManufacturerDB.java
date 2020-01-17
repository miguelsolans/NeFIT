package db;

import business.Manufacturer;

import java.util.HashMap;

public class ManufacturerDB {

    static HashMap<String, Manufacturer> manufacturers = new HashMap<>();

    static {
        manufacturers.put("Korg", new Manufacturer("Korg"));
        manufacturers.put("Peugeot", new Manufacturer("Peugeot"));
    }

    public static HashMap<String, Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public static Manufacturer getSingleManufacturer(String manufacturer) {
        return manufacturers.get(manufacturer);
    }

    public static void addManufacturer(Manufacturer manufacturer) {
        manufacturers.put(manufacturer.getName(), manufacturer);
    }
}
