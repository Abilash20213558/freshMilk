package com.example.mad_assignment;

import android.text.Editable;

import com.google.firebase.database.Exclude;

public class AdminProductModel {
    private String ProdName;
    private String imageURL;
    private String unitPrice;
    private String description;
    private String liter;


    public AdminProductModel() {
        //Empty constructor
    }



    public AdminProductModel(String productName, String mimageURL, String munitPrice, String mdescription, String mliter) {
        if(productName.trim().equals("")){
            productName = "NO Name";
        }


        ProdName = productName;
        imageURL = mimageURL;
        unitPrice = munitPrice;
        description = mdescription;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }

}
