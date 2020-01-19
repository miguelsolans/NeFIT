package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Importer {
    private String username;
    private HashMap<Integer, ItemOrderOffer> orders;
    // private List<ItemOrderOffer> orders;
    // private static int orderId;

    public Importer(String username) {
        this.username = username;
        this.orders = new HashMap<>();
        // orderId = 0;
        // this.orders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ItemOrderOffer> getOrders() {
        List<ItemOrderOffer> item = new ArrayList<>();

        for(Integer key : this.orders.keySet()) {
            item.add(this.orders.get(key));
        }

        return item;
    }

    public ItemOrderOffer getOrder(int id) {
        return this.orders.get(id);
    }

    public void newOrder(ItemOrderOffer order) {
        // order.setOrderId(orderId);
        this.orders.put(order.getOrderId(), order);
        // orderId++;
    }
}
