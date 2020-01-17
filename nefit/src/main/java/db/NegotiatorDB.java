package db;

import business.Negotiator;

import java.util.HashMap;

public class NegotiatorDB {

    static HashMap<String, Negotiator> negotiatorHashMap = new HashMap<>();

    static {
        addNegotiator(new Negotiator("teste", "127.0.0.1", "8080"));
    }

    public static void addNegotiator(Negotiator n) {
        negotiatorHashMap.put(n.getName(), n);
    }

    public static Negotiator getNegotiator(String name) {
        return negotiatorHashMap.get(name);
    }


}
