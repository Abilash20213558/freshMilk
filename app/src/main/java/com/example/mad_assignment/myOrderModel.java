package com.example.mad_assignment;

public class myOrderModel {
    private String ProdName;
    private String imageURL;
    private String unitPrice;
    private String quantity;
    private String totalAmount;
    private String liter;

    public myOrderModel() {
        //Empty constructor
    }

    public myOrderModel(String productName, String mimageURL, String munitPrice, String mquantity, String mtotAmount ,String mliter) {
        if(productName.trim().equals("")){
            productName = "NO Name";
        }


        ProdName = productName;
        imageURL = mimageURL;
        unitPrice = munitPrice;
        quantity = mquantity;
        totalAmount = mtotAmount;
        liter = mliter;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }
}
