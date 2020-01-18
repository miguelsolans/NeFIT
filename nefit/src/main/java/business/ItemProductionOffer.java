package business;

import java.util.Date;

public class ItemProductionOffer {
    String productName;
    String manufacturerName;
    double unitPrice;
    double minimumAmout;
    double maximumAmount;
    Date start;
    Date end;


    public ItemProductionOffer(String productName, String manufacturerName, double unitPrice, double minimumAmout, double maximumAmount, int period) {
        this.productName = productName;
        this.manufacturerName = manufacturerName;
        this.unitPrice = unitPrice;
        this.minimumAmout = minimumAmout;
        this.minimumAmout = maximumAmount;

        this.start = new Date();
        // this.end = new Date // Calc last day of offer
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

    public Date getStart() {
        return this.start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return this.end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
