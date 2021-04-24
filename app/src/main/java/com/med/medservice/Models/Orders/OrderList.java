package com.med.medservice.Models.Orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderList implements Serializable {


    public String id;
    public String order_state;
    public String order_id;
    public OrderSubId order_sub_id;
    public String customer_id;
    public String total;
    public String shipping_total;
    public String total_tax;
    public Billing billing;
    public Shipping shipping;
    public Payment payment;
    public String payment_title;
    public String payment_method;
    public ArrayList<CartItem> cart_items;
    public String tax_lines;
    public String shipping_lines;
    public String coupon_lines;
    public String currency;
    public String order_status;
    public String deleted_at;
    public String created_at;
    public String updated_at;

    public OrderList(String id, String order_id,  String customer_id, String total,
                     Billing billing, Shipping shipping, String payment_title, String payment_method, ArrayList<CartItem>
                             cart_items, String order_status, String created_at) {
        this.id = id;
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.total = total;
        this.billing = billing;
        this.shipping = shipping;
        this.payment_title = payment_title;
        this.payment_method = payment_method;
        this.cart_items = cart_items;
        this.order_status = order_status;
        this.created_at = created_at;
    }

    public OrderList(String id, String order_state, String order_id, OrderSubId order_sub_id, String customer_id, String total,
                     String shipping_total, String total_tax, Billing billing, Shipping shipping, Payment payment, String payment_title,
                     String payment_method, ArrayList<CartItem> cart_items, String tax_lines, String shipping_lines, String coupon_lines, String currency, String order_status, String deleted_at, String created_at, String updated_at) {
        this.id = id;
        this.order_state = order_state;
        this.order_id = order_id;
        this.order_sub_id = order_sub_id;
        this.customer_id = customer_id;
        this.total = total;
        this.shipping_total = shipping_total;
        this.total_tax = total_tax;
        this.billing = billing;
        this.shipping = shipping;
        this.payment = payment;
        this.payment_title = payment_title;
        this.payment_method = payment_method;
        this.cart_items = cart_items;
        this.tax_lines = tax_lines;
        this.shipping_lines = shipping_lines;
        this.coupon_lines = coupon_lines;
        this.currency = currency;
        this.order_status = order_status;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public String  getId() {
        return id;
    }

    public String getOrder_state() {
        return order_state;
    }

    public String getOrder_id() {
        return order_id;
    }

    public OrderSubId getOrder_sub_id() {
        return order_sub_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getTotal() {
        return total;
    }

    public String getShipping_total() {
        return shipping_total;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public Billing getBilling() {
        return billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public Payment getPayment() {
        return payment;
    }

    public String getPayment_title() {
        return payment_title;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public List<CartItem> getCart_items() {
        return cart_items;
    }

    public String getTax_lines() {
        return tax_lines;
    }

    public String getShipping_lines() {
        return shipping_lines;
    }

    public String getCoupon_lines() {
        return coupon_lines;
    }

    public String getCurrency() {
        return currency;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

}


