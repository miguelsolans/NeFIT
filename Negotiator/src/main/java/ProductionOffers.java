import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionOffers {

    private Map<String, Map<String,ProductionOffer>> productionOffers;
    private int id;

    public ProductionOffers() {
        this.productionOffers = new HashMap<>();
        this.id = 0;
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
            if (!offers.containsKey(productionOffer.getArticleName()) ){
                offers.put(productionOffer.getArticleName(),productionOffer);
                this.productionOffers.put(productionOffer.getFabricantName(),offers);
                return true;
            }
            ProductionOffer offer = offers.get(productionOffer.getArticleName());
            if (offer!= null && !offer.getActive()){
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
                    offer.setId(this.id++);
                    productionOffer.addOrder(offer);
                    this.productionOffers.replace(offer.getFabricantName(), offers);
                    return true;
                }
            }
        }
        return false;
    }

    public int getProductionOrderId (String manufacturer, String product, String username) {

        if(this.productionOffers.containsKey(manufacturer)) {

            Map<String, ProductionOffer> offer = this.productionOffers.get(manufacturer);

            if(offer.containsKey(product)) {
                ProductionOffer productionOfferAux = offer.get(product);

                List<Offer> offerList = productionOfferAux.getOffers();

                for(Offer offerSingle : offerList) {

                    if(offerSingle.getFabricantName().equals(manufacturer) && offerSingle.getUserName().equals(username)) {
                        return offerSingle.getId();
                    }
                }
                // return offerAux.getId();
            }
        }

        return -1;
    }

}
