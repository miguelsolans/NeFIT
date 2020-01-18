import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import Protos.Protocol;

public class ProductionOffer {
    
    private String fabricantName;
    private String articleName;
    private Float quantMIN;
    private Float quantMax;
    private Float unitPrice;
    private Boolean isActive;
    private ZMQ.Socket push;
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
     * @param push
     */
    public ProductionOffer(String fabricantName, String articleName, Float quantMIN, Float quantMax, float unitPrice, int time, Boolean isActive, ZMQ.Socket push) {
        this.fabricantName = fabricantName;
        this.articleName = articleName;
        this.quantMIN = quantMIN;
        this.quantMax = quantMax;
        this.unitPrice = unitPrice;
        this.isActive = isActive;
        this.push = push;
        this.offers = new ArrayList<>();
        new Timer().schedule(new Finisher(this),time);
    }

    public void addOrder(Offer offer){
        offers.add(offer);
    }

    public void finisher(){
        this.setActive(false);
        this.offers.sort(new ComparatorOffers());
        float quantity;
        Protocol.User manufactureUser = Protocol.User.newBuilder().
                                                setUsername(this.fabricantName).
                                                build();
        for (Offer offer : this.offers){
            quantity = this.quantMax - offer.getQuantity();

            if (quantity>= this.quantMIN) {
                this.quantMax -= offer.getQuantity();
                Protocol.User userOffer = Protocol.User.newBuilder().
                                                    setUsername(offer.getUserName()).
                                                    build();
                Protocol.Sale sale = Protocol.Sale.newBuilder().
                                                    setArticleName(this.articleName).
                                                    setManufactureName(this.fabricantName).
                                                    setOfferName(offer.getUserName()).
                                                    setGlobalPrice((float)offer.getGlobalPrice()).
                                                    setMessage("Winner").build();
                Protocol.Message messageO = Protocol.Message.newBuilder().
                                                    setUser(userOffer).
                                                    setType(Protocol.Type.RESPONSE).
                                                    setSale(sale).
                                                    build();
                Protocol.Message messageP = Protocol.Message.newBuilder().
                                                    setUser(manufactureUser).
                                                    setType(Protocol.Type.RESPONSE).
                                                    setSale(sale).
                                                    build();
                push.send(messageP.toByteArray());
                push.send(messageO.toByteArray());
            }
            else {
                Protocol.User userOffer = Protocol.User.newBuilder().
                        setUsername(offer.getUserName()).
                        build();
                Protocol.Sale sale = Protocol.Sale.newBuilder().
                                                setOfferName(offer.getUserName()).
                                                setMessage("Loser").
                                                build();
                Protocol.Message messageO = Protocol.Message.newBuilder().
                                                    setUser(userOffer).
                                                    setSale(sale).
                                                    setType(Protocol.Type.RESPONSE).
                                                    build();
                Protocol.Message messageP = Protocol.Message.newBuilder().
                                                    setUser(manufactureUser).
                                                    setType(Protocol.Type.RESPONSE).
                                                    setSale(sale).
                                                    build();
                push.send(messageO.toByteArray());
                push.send(messageP.toByteArray());
            }
        }

    }

    //Getters & Setters
    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getFabricantName() {
        return this.fabricantName;
    }

    public String getArticleName() { return this.articleName; }

    public Float getQuantMIN() { return this.quantMIN;}

    public boolean getActive() { return this.isActive; }

    public Float getQuantMax() { return this.quantMax; }
}
