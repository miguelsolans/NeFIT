package Model;

public class Offer {

    private String fabricantName;
    private String articleName;
    private Float quantity;
    private Float unitPrice;
    private Boolean isValid;
    private Float globalPrice;

    public Offer(String fabricantName, String articleName, Float quantity, Float unitPrice,Boolean isValid) {
        this.fabricantName = fabricantName;
        this.articleName = articleName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.isValid = isValid;
        this.globalPrice = this.quantity * this.unitPrice;
    }

    // Getters & Setters
    public void setQuantity(Float quantity) { this.quantity = quantity; }

    public Float getGlobalPrice() { return globalPrice; }

    public void setGlobalPrice(Float globalPrice) { this.globalPrice = globalPrice; }

    public Float getQuantity() { return this.quantity; }

    public String getFabricantName() { return this.fabricantName; }

    public String getArticleName() { return this.articleName; }

}
