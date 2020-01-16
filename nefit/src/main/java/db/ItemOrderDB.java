package db;

import business.ItemOrderOffer;

import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.List;

public class ItemOrderDB {

    static List<ItemOrderOffer> itemOrders = new ArrayList<>();

    // For Debugging, delete afterwards
    static {
        addOrder(new ItemOrderOffer("manufacturerName", "productName", 1.0, 1.0));
        addOrder(new ItemOrderOffer("Company", "Some product", 5.0, 23.3));
    }

    public static List<ItemOrderOffer> getOrders() {
        return itemOrders;
    }

    public static void addOrder(ItemOrderOffer o) {
        itemOrders.add(o);
    }

}
