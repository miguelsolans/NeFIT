package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class ProductionOffer {
    private String articleName;
    private Float quantMIN;
    private Float quantMax;
    private Float unitPrice;
    private Boolean isActive;
    private List<Offer> offers;


    /**
     *
     * @param fabricantName
     * @param articleName
     * @param quantMIN
     * @param quantMax
     * @param unitPrice
     * @param time in milliseconds
     * @param isActive
     */
    public ProductionOffer(String fabricantName, String articleName, Float quantMIN, Float quantMax, float unitPrice, int time, Boolean isActive) {
        this.articleName = articleName;
        this.quantMIN = quantMIN;
        this.quantMax = quantMax;
        this.unitPrice = unitPrice;
        this.isActive = isActive;
        this.offers = new ArrayList<>();
        // new Timer().schedule(new Finisher(this),time);
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Float getQuantMIN() {
        return quantMIN;
    }

    public void setQuantMIN(Float quantMIN) {
        this.quantMIN = quantMIN;
    }

    public Float getQuantMax() {
        return quantMax;
    }

    public void setQuantMax(Float quantMax) {
        this.quantMax = quantMax;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void addOffer(Offer offer) {
        this.offers.add(offer);
    }
}
