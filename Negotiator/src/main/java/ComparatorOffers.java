import java.util.Comparator;

public class ComparatorOffers implements Comparator<Offer> {

    @Override
    public int compare(Offer offer1, Offer offer2) {
        Float globalPrice1 = offer1.getGlobalPrice();
        Float glovalPrice2 = offer2.getGlobalPrice();

        if (globalPrice1>=glovalPrice2) return (-1);
        else return 1;
    }
}
