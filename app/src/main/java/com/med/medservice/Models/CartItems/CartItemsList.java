package com.med.medservice.Models.CartItems;

import java.io.Serializable;

public class CartItemsList implements Serializable {

    String ID, USER_ID, ITEM_ID, NAME, QUANTITY, PRICE, DISCOUNT, TYPE, IMAGE;

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getITEM_ID() {
        return ITEM_ID;
    }

    public void setITEM_ID(String ITEM_ID) {
        this.ITEM_ID = ITEM_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(String QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(String DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public CartItemsList() {
    }

    public CartItemsList(String ID, String USER_ID, String ITEM_ID, String NAME, String QUANTITY, String PRICE, String DISCOUNT, String TYPE) {
        this.ID = ID;
        this.USER_ID = USER_ID;
        this.ITEM_ID = ITEM_ID;
        this.NAME = NAME;
        this.QUANTITY = QUANTITY;
        this.PRICE = PRICE;
        this.DISCOUNT = DISCOUNT;
        this.TYPE = TYPE;
    }

    public CartItemsList(String ID, String USER_ID, String ITEM_ID, String NAME, String QUANTITY, String PRICE, String DISCOUNT, String TYPE, String IMAGE) {
        this.ID = ID;
        this.USER_ID = USER_ID;
        this.ITEM_ID = ITEM_ID;
        this.NAME = NAME;
        this.QUANTITY = QUANTITY;
        this.PRICE = PRICE;
        this.DISCOUNT = DISCOUNT;
        this.TYPE = TYPE;
        this.IMAGE = IMAGE;
    }
}
