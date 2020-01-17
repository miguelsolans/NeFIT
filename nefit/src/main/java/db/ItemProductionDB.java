package db;

import business.ItemProductionOffer;

import java.util.ArrayList;
import java.util.List;

public class ItemProductionDB {

    static List<ItemProductionOffer> products = new ArrayList<>();

//    static {
//        // periodo em segundos ????
//        addProduct(new ItemProductionOffer("product1", "manufacturer1", 1.0, 1.0, 1.0, 50000));
//        addProduct(new ItemProductionOffer("product2", "manufacturer1", 1.0, 1.0, 1.0, 402));
//        addProduct(new ItemProductionOffer("product1", "manufacturer2", 1.0, 1.0, 1.0, 1234));
//
//    }

    public static List<ItemProductionOffer> getAvailableProducts() {
        return products;
    }

    public static void addProduct(ItemProductionOffer product) {
        products.add(product);
    }

    public static List<ItemProductionOffer> getManufacturerProducts(String manufacturer) {
        List<ItemProductionOffer> manufacturerProducts = new ArrayList<>();

        for(ItemProductionOffer p : products)
            if (p.getManufacturerName().equals(manufacturer))
                manufacturerProducts.add(p);

        return manufacturerProducts;
    }
}
