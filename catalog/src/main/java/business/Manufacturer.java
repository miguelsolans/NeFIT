package business;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manufacturer {

    @NotNull
    private String name;
    @NotNull
    private Negotiator negotiator;
//    @NotNull
//    private String host;
//    @NotNull
//    private String port;

    private HashMap<String, ItemProductionOffer> products;
    private static int productId;

    public Manufacturer(String name) {
        this.name = name;
    }

    public Manufacturer(String name, Negotiator negotiator) {
        this.name = name;
        this.negotiator = negotiator;
        this.products = new HashMap<>();
        productId = 0;
    }

    public void addProduct(ItemProductionOffer product) {
        this.products.put(product.getName(), product);
    }

    public String getName() {
        return name;
    }

    public List<ItemProductionOffer> getProducts() {
        List<ItemProductionOffer> products = new ArrayList<>();

        for(String key : this.products.keySet()) {
            products.add(this.products.get(key));
        }

        return products;
    }

    public List<ItemProductionOffer> getActiveOffers() {
        List<ItemProductionOffer> productionOffers = new ArrayList<>();

        for(String key : this.products.keySet()) {
            if(this.products.get(key).isActive())
                productionOffers.add(this.products.get(key));
        }

        return productionOffers;
    }

    public ItemProductionOffer getProduct(String name) {
        return this.products.get(name);
    }

    public Negotiator getNegotiator() {
        return this.negotiator;
    }

    public void setNegotiator(Negotiator negotiator) {
        this.negotiator = negotiator;
    }
}
