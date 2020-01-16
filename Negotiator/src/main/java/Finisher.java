import java.util.List;
import java.util.TimerTask;

public class Finisher extends TimerTask {

    private ProductionOffer productionOffer;

    public Finisher(ProductionOffer productionOffer){
        this.productionOffer = productionOffer;
    }

    @Override
    public void run() {
        this.productionOffer.finisher();
    }
}
