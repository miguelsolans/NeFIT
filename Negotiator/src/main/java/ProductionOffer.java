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
    private Sender sender;
    private int id;


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
        this.sender = new Sender();
        this.id = 0;
    }

    public void addOrder(Offer offer){
        // offer.setId(this.id++);
        offers.add(offer);
    }

    public void finisher(){
        this.setActive(false);
        this.offers.sort(new ComparatorOffers());
        float quantity;
        int winners = 0, losers=0;
        Protocol.User manufactureUser = Protocol.User.newBuilder().
                setUsername(this.fabricantName).
                build();
        quantity = this.quantMax;
        for (Offer offer : this.offers){

            if (offer.getQuantity()>= this.quantMIN && offer.getQuantity()<=this.quantMax
                    && quantity>=this.quantMIN && quantity-offer.getQuantity()>=0) {
                winners++;
                quantity -= offer.getQuantity();

                sender.sendWinnerOffer(offer.getUserName(), offer.getId());

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
                        setType(Protocol.Type.NOTIFICATION).
                        setSale(sale).
                        build();
                Protocol.Message messageP = Protocol.Message.newBuilder().
                        setUser(manufactureUser).
                        setType(Protocol.Type.NOTIFICATION).
                        setSale(sale).
                        build();
                this.push.send(messageP.toByteArray());
                this.push.send(messageO.toByteArray());
            }
            else {
                losers++;
                Protocol.User userOffer = Protocol.User.newBuilder().
                        setUsername(offer.getUserName()).
                        build();
                Protocol.Sale sale = Protocol.Sale.newBuilder().
                        setArticleName(this.articleName).
                        setManufactureName(this.fabricantName).
                        setOfferName(offer.getUserName()).
                        setMessage("Loser").build();
                Protocol.Message messageO = Protocol.Message.newBuilder().
                        setUser(userOffer).
                        setSale(sale).
                        setType(Protocol.Type.NOTIFICATION).
                        build();
                Protocol.Message messageP = Protocol.Message.newBuilder().
                        setUser(manufactureUser).
                        setType(Protocol.Type.NOTIFICATION).
                        setSale(sale).
                        build();
                push.send(messageO.toByteArray());
                push.send(messageP.toByteArray());

            }
        }
        sender.sendCloseNegotiation(this.fabricantName,this.articleName);
        System.out.println("Lnçamento de produção finalizado com "+winners+" vencedores e com " +losers+" perdedores");

    }

    //Getters & Setters
    public void setActive(Boolean active) { isActive = active; }

    public String getFabricantName() { return this.fabricantName;}

    public String getArticleName() { return this.articleName; }

    public Float getQuantMIN() { return this.quantMIN;}

    public boolean getActive() { return this.isActive; }

    public Float getQuantMax() { return this.quantMax; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public List<Offer> getOffers() {
        return offers;
    }
}