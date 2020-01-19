package db;

import business.ItemOrderOffer;
import business.ItemProductionOffer;
import business.Manufacturer;
import business.Negotiator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManufacturerDB {

    static HashMap<String, Manufacturer> manufacturers = new HashMap<>();
    static HashMap<String, Negotiator> negotiators = new HashMap<>();

    static {
        negotiators.put("4001", new Negotiator("Neg1", "127.0.0.1", "4001"));
        negotiators.put("4002", new Negotiator("Neg2", "127.0.0.1", "4002"));
        negotiators.put("4003", new Negotiator("Neg3", "127.0.0.1", "4003"));

        manufacturers.put("Korg", new Manufacturer("Korg", negotiators.get("4001")));
        manufacturers.put("Peugeot", new Manufacturer("Peugeot", negotiators.get("4002")));
        manufacturers.put("Land Rover", new Manufacturer("Land Rover", negotiators.get("4003")));

        manufacturers.get("Peugeot").addProduct(new ItemProductionOffer("208", "Peugeot", 17000.0, 1, 100, 5000));
        manufacturers.get("Peugeot").addProduct(new ItemProductionOffer("308", "Peugeot",20000.0, 1, 20, 60));
        manufacturers.get("Land Rover").addProduct(new ItemProductionOffer("Discovery", "Land Rover", 50000.0, 1, 500, 10));
        manufacturers.get("Land Rover").addProduct(new ItemProductionOffer("Defender", "Land Rover", 100000.0, 1, 150, 30));
        manufacturers.get("Land Rover").addProduct(new ItemProductionOffer("Serie", "Land Rover", 105000.0, 3, 2, 90));


        manufacturers.get("Korg").addProduct(new ItemProductionOffer("Kronos", "Korg", 25000, 1, 2500, 1));

        manufacturers.get("Korg").getProduct("Kronos").setActive(false);
    }

    public static HashMap<String, Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public static List<ItemProductionOffer> getAvailableProducts(String mame) {

        List<ItemProductionOffer> products = new ArrayList<>();

        Manufacturer manufacturer = manufacturers.get(mame);
        if(manufacturer != null) {
            for(ItemProductionOffer product : manufacturer.getProducts()) {
                if(product.isActive())
                    products.add(product);
            }
        }


        return products;
    }

    public static List<ItemProductionOffer> getAllProducts() {
        List<ItemProductionOffer> products = new ArrayList<>();

        for(String key : manufacturers.keySet()) {
            products.addAll(manufacturers.get(key).getProducts());
        }

        return products;
    }

    public static List<ItemProductionOffer> getAvailableProducts() {

        List<ItemProductionOffer> products = new ArrayList<>();

        for(String key : manufacturers.keySet()) {
            List<ItemProductionOffer> manufacturerProducts = manufacturers.get(key).getProducts();

            for(ItemProductionOffer offer : manufacturerProducts) {
                if(offer.isActive())
                    products.add(offer);
            }
        }

        return products;
    }

    public static Manufacturer getSingleManufacturer(String manufacturer) {
        return manufacturers.get(manufacturer);
    }

    public static void addManufacturer(Manufacturer manufacturer, String host, String port) {
        manufacturer.setNegotiator(negotiators.get(port));

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
