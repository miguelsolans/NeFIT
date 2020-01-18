public class Offer {

    private String userName;
    private String fabricantName;
    private String articleName;
    private Float quantity;
    private Float unitPrice;
    private Float globalPrice;

    public Offer(String fabricantName, String articleName, Float quantity, Float unitPrice,String userName) {
        this.fabricantName = fabricantName;
        this.articleName = articleName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.globalPrice = this.quantity * this.unitPrice;
        this.userName = userName;
    }

    // Getters & Setters

    public Float getGlobalPrice() { return globalPrice; }

    public Float getQuantity() { return this.quantity; }

    public String getFabricantName() { return this.fabricantName; }

    public String getArticleName() { return this.articleName; }

    public String getUserName() { return userName;}
}
