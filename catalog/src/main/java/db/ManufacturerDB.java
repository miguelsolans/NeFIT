package db;

import business.ItemProductionOffer;
import business.Manufacturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManufacturerDB {

    static HashMap<String, Manufacturer> manufacturers = new HashMap<>();

    static {
        // String portPUSH = "12345", portPULL="12346",portPUB="12347";
        manufacturers.put("Korg", new Manufacturer("Korg", "127.0.0.1", "12344"));
        manufacturers.put("Peugeot", new Manufacturer("Peugeot", "127.0.0.1", "12346"));
        manufacturers.put("Land Rover", new Manufacturer("Land Rover", "127.0.0.1", "1237"));

        // String productName, double unitPrice, double minimumAmout, double maximumAmount, int period
        manufacturers.get("Peugeot").addProduct(new ItemProductionOffer("208", 17000.0, 1, 100, 5000));
        manufacturers.get("Peugeot").addProduct(new ItemProductionOffer("308", 20000.0, 1, 20, 60));
        manufacturers.get("Land Rover").addProduct(new ItemProductionOffer("Discovery", 50000.0, 1, 500, 10));
        manufacturers.get("Land Rover").addProduct(new ItemProductionOffer("Defender", 100000.0, 1, 150, 30));
        manufacturers.get("Land Rover").addProduct(new ItemProductionOffer("Serie", 105000.0, 3, 2, 90));
    }

    public static HashMap<String, Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public static List<ItemProductionOffer> getAvailableProducts() {
        List<ItemProductionOffer> products = new ArrayList<>();

        for(String key : manufacturers.keySet()) {
            products.addAll(manufacturers.get(key).getProducts());
        }

        return products;
    }

    public static Manufacturer getSingleManufacturer(String manufacturer) {
        return manufacturers.get(manufacturer);
    }

    public static void addManufacturer(Manufacturer manufacturer) {
        manufacturers.put(manufacturer.getName(), manufacturer);
    }

    public static List<ItemProductionOffer> getManufacturerProducts(String manufacturer) {
        return manufacturers.get(manufacturer).getProducts();
    }

    public static void addProduct(String manufacturer, ItemProductionOffer product) {
        manufacturers.get(manufacturer)
                .addProduct(product);
    }
}
