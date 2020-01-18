package business;

import java.util.ArrayList;
import java.util.List;

public class Importer {
    private String username;
    private List<ItemOrderOffer> orders;


    public Importer(String username) {
        this.username = username;
        this.orders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ItemOrderOffer> getOrders() {
        return orders;
    }

    public void newOrder(ItemOrderOffer order) {
        this.orders.add(order);
    }
}
