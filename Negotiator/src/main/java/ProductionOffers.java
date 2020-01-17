import org.zeromq.ZMQ;

import java.util.HashMap;
import java.util.Map;

public class ProductionOffers {

    private Map<String, Map<String,ProductionOffer>> productionOffers;

    public ProductionOffers(){
        this.productionOffers = new HashMap<>();
    }


    //Insert new productionOffer
    public boolean insertProductionOffert(ProductionOffer productionOffer){
        if(!this.productionOffers.containsKey(productionOffer.getFabricantName())){
            Map<String,ProductionOffer> offers = new HashMap<>();
            offers.put(productionOffer.getArticleName(),productionOffer);
            this.productionOffers.put(productionOffer.getFabricantName(),offers);
            return true;
        }
        else {
            Map<String,ProductionOffer> offers = this.productionOffers.get(productionOffer.getFabricantName());
            if (!offers.containsKey(productionOffer.getArticleName())){
                offers.put(productionOffer.getArticleName(),productionOffer);
                this.productionOffers.put(productionOffer.getFabricantName(),offers);
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
