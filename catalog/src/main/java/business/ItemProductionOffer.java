package business;

import org.joda.time.DateTime;

public class ItemProductionOffer {

    String productName;
    String manufacturerName;
    double unitPrice;
    double minimumAmout;
    double maximumAmount;
    DateTime start;
    DateTime end;


    public ItemProductionOffer(String productName, double unitPrice, double minimumAmout, double maximumAmount, int period) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.minimumAmout = minimumAmout;
        this.minimumAmout = maximumAmount;

        this.start = new DateTime();
        this.end = new DateTime().plusSeconds(period);
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getName() {
        return this.productName;
    }

    public void setName(String name) {
        this.productName = name;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getMinimumAmout() {
        return this.minimumAmout;
    }

    public void setMinimumAmout(float minimumAmout) {
        this.minimumAmout = minimumAmout;
    }

    public double getMaximumAmount() {
        return this.maximumAmount;
    }

    public void setMaximumAmount(float maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public DateTime getStart() {
        return this.start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return this.end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }
}
