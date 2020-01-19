import org.zeromq.ZMQ;

import java.util.HashMap;
import java.util.Map;

public class ProductionOffers {

    private Map<String, Map<String,ProductionOffer>> productionOffers;
    private int id;

    public ProductionOffers(){
        this.productionOffers = new HashMap<>();
        this.id = 0;
    }


    //Insert new productionOffer
    public boolean insertProductionOffert(ProductionOffer productionOffer){
        if(!this.productionOffers.containsKey(productionOffer.getFabricantName())){
            Map<String,ProductionOffer> offers = new HashMap<>();
            productionOffer.setId(this.id++);
            offers.put(productionOffer.getArticleName(),productionOffer);
            this.productionOffers.put(productionOffer.getFabricantName(),offers);
            return true;
        }
        else {
            Map<String,ProductionOffer> offers = this.productionOffers.get(productionOffer.getFabricantName());
            if (!offers.containsKey(productionOffer.getArticleName()) ){
                productionOffer.setId(this.id++);
                offers.put(productionOffer.getArticleName(),productionOffer);
                this.productionOffers.put(productionOffer.getFabricantName(),offers);
                return true;
            }
            ProductionOffer offer = offers.get(productionOffer.getArticleName());
            if (offer!= null && !offer.getActive()){
                productionOffer.setId(this.id++);
                offers.replace(productionOffer.getArticleName(),productionOffer);
                this.productionOffers.replace(productionOffer.getFabricantName(),offers);
                return true;
            }
            else return false;
        }
    }

    //Insert a new offer
    public boolean insertItemOrderOffer(Offer offer){
        if (this.productionOffers.containsKey(offer.getFabricantName())){
            Map<String, ProductionOffer> offers = this.productionOffers.get(offer.getFabricantName());
            if (offers.containsKey(offer.getArticleName()) ){
                ProductionOffer productionOffer = offers.get(offer.getArticleName());
                if (productionOffer.getActive() && !(offer.getQuantity()<productionOffer.getQuantMIN()) && offer.getQuantity()<=productionOffer.getQuantMax()) {
                    productionOffer.addOrder(offer);
                    this.productionOffers.replace(offer.getFabricantName(), offers);
                    return true;
                }
            }
        }
        return false;
    }

}
