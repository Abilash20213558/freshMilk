package com.example.madp;

public class CardInfo {


    String cardnum;
    String date;
    String cvv;
    String nic;

    public CardInfo (String nic, String cardnum,String date,String cvv){
        this.nic = nic;
        this.cardnum = cardnum;
        this.date = date;
        this.cvv = cvv;
    }

    public String getNic() {
        return nic;
    }

    public String getCardnum() {
        return cardnum;
    }

    public String getDate() {
        return date;
    }

    public String getCvv() {
        return cvv;
    }
}
