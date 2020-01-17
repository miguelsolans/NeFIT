import org.zeromq.ZMQ;

import Protos.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
      //  System.out.println("Acabou MESMO");
        this.setActive(false);
        this.offers.sort(new ComparatorOffers());
        float quantity;
        for (Offer offer : this.offers){
            quantity = this.quantMax - offer.getQuantity();
           // System.out.println("maxQuant="+this.quantMax+" Minimun="+this.quantMIN+" Quantity: "+quantity);
            if (quantity>= this.quantMIN) {
                System.out.println("Winner");
                // falta enviar para o producer
                this.quantMax -= offer.getQuantity();
                Protocol.Sale sale = Protocol.Sale.newBuilder().
                        setArticleName(this.articleName).
                        setManufactureName(this.fabricantName).
                        setGlobalPrice((float)offer.getGlobalPrice()).
                        setMessage("Winner").build();
                Protocol.Message message = Protocol.Message.newBuilder().
                                                        setType(Protocol.Type.RESPONSE).
                                                        setSale(sale).
                                                        build();
                push.send(message.toByteArray());
            }
            else {
               // System.out.println("Loser");
                Protocol.Sale sale = Protocol.Sale.newBuilder().
                                                setMessage("Loser").
                                                build();
                Protocol.Message message = Protocol.Message.newBuilder().
                                                    setSale(sale).
                                                    setType(Protocol.Type.RESPONSE).
                                                    build();
                push.send(message.toByteArray());
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
