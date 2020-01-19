package business;

import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

public class ItemProductionOffer {
    @NotNull
    String productName;
    @NotNull
    String manufacturer;
    @NotNull
    double unitPrice;
    @NotNull
    double minimumAmount;
    @NotNull
    double maximumAmount;
    @NotNull
    DateTime start;
    @NotNull
    DateTime end;
    @NotNull
    boolean active;


    public ItemProductionOffer(String productName, String manufacturer, double unitPrice, double minimumAmount, double maximumAmount, int period) {
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.active = true;
        this.start = new DateTime();
        this.end = new DateTime().plusSeconds(period);
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
        return this.minimumAmount;
    }

    public void setMinimumAmout(float minimumAmout) {
        this.minimumAmount = minimumAmout;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
