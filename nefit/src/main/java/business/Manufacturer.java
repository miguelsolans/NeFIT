package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manufacturer {

    private String name;
    private HashMap<String, ItemProductionOffer> products;


    public Manufacturer(String name) {
        this.products = new HashMap<>();
    }

    public void addProduct(ItemProductionOffer product) {
        this.products.put(product.productName, product);
    }

    public String getName() {
        return name;
    }

    public HashMap<String, ItemProductionOffer> getProducts() {
        List<ItemProductionOffer> products = new ArrayList<>();

        for(String key : this.products.keySet()) {
            products.add(this.products.get(key));
        }

        return this.products;
    }


}
