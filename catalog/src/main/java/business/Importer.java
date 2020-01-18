package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Importer {
    private String username;
    private List<ItemOrderOffer> orders;
    // private List<ItemOrderOffer> orders;
    private static int orderId;

    public Importer(String username) {
        this.username = username;
        this.orders = new ArrayList<>();
        orderId = 0;
        // this.orders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ItemOrderOffer> getOrders() {
        return this.orders;
    }

    public ItemOrderOffer getOrder(int id) {
        if( id < this.orders.size() )
            return this.orders.get(id);
        return null;
    }

    public void newOrder(ItemOrderOffer order) {
        order.setOrderId(orderId);
        this.orders.add(order);
        orderId++;
    }

    public List<ItemOrderOffer> getActiveOrders() {
        List<ItemOrderOffer> offers = new ArrayList<>();
        for(ItemOrderOffer offer : this.orders) {
            if(!offer.isFinished())
                offers.add(offer);
        }

        return offers;
    }
}
