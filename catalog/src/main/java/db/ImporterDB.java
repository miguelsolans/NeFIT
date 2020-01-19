package db;

import business.Importer;
import business.ItemOrderOffer;
import business.ItemProductionOffer;

import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImporterDB {
    static HashMap<String, Importer> importers = new HashMap<>();

    // TODO: Delete aftewards
    static {
        addImporter( new Importer("miguelsolans") );
        addImporter( new Importer("tifanysilva") );
        addImporter( new Importer("henriquepereira") );
        addImporter( new Importer("joaosilva") );
    }

    public static void addImporter(Importer importer) {
        importers.put(importer.getUsername(), importer);
    }

    public static List getImporters() {
        List<Importer> i = new ArrayList<>();
        for(String key : importers.keySet())
            i.add(importers.get(key));

        return i;
    }

    public static Importer getSingleImporter(String name) {
        return importers.get(name);
    }

    public static List getImporterOrder(String importer) {
        return importers.get(importer).getOrders();
    }

    public static List getAllOrders() {
        List<ItemOrderOffer> orders = new ArrayList<>();

        for(String key : importers.keySet()) {
            orders.addAll(importers.get(key).getOrders());
        }

        return orders;
    }

    public static void newOrder(String importer, ItemOrderOffer order) {
        importers.get(importer)
                .newOrder(order);
    }

    public static void setWinner(String user, int orderId) {


        ItemOrderOffer t = importers.get(user).getOrder(orderId);

        t.setWinner();

        /*importers.get(user)
                .getOrder(orderId)
                .setWinner();*/
    }

}