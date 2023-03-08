package com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCart;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AbandonedCart {
    private Long cartId;
    private List<String> items;
    private String customer;
    private String customerEmail;
    private URL recoverUrl;
    private Date abandonedTime;
    private Date abandonedStart;

    public long getAbandonedStartInMs() {
        Date currTime = this.abandonedStart;
        Calendar calender = Calendar.getInstance();
        calender.setTime(currTime);
        long timeInMs = calender.getTimeInMillis();
        return timeInMs;
    }

    public Date getAbandonedTime() {
        return abandonedTime;
    }

    public void setAbandonedTime(Date abandonedTime) {
        this.abandonedTime = abandonedTime;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public URL getRecoverUrl() {
        return recoverUrl;
    }

    public void setRecoverUrl(URL recoverUrl) {
        this.recoverUrl = recoverUrl;
    }

    public AbandonedCart(Long cartId, List<String> items, String customer, String customerEmail, URL recoverUrl,
            Date abandonedTime) {
        this.cartId = cartId;
        this.items = items;
        this.customer = customer;
        this.customerEmail = customerEmail;
        this.recoverUrl = recoverUrl;
        this.abandonedTime = abandonedTime;
        this.abandonedStart = abandonedTime;
    }

}
