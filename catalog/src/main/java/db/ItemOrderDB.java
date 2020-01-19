package db;

import business.ItemOrderOffer;

import java.util.ArrayList;
import java.util.List;

public class ItemOrderDB {

    static List<ItemOrderOffer> itemOrders = new ArrayList<>();

    public static List<ItemOrderOffer> getOrders() {
        return itemOrders;
    }

    public static void addOrder(ItemOrderOffer o) {
        itemOrders.add(o);
    }

}
