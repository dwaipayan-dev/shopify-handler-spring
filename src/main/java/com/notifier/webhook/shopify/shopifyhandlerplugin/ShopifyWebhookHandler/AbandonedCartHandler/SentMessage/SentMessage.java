package com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.SentMessage;

import java.net.URL;
import java.util.Date;
import java.util.List;

public class SentMessage {
    private String recipient;
    private Date onDate;
    private List<String> items;
    private URL recoverUrl;

    public SentMessage(String recipient, Date onDate, List<String> items, URL recoverUrl) {
        this.recipient = recipient;
        this.onDate = onDate;
        this.items = items;
        this.recoverUrl = recoverUrl;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public URL getRecoverUrl() {
        return recoverUrl;
    }

    public void setRecoverUrl(URL recoverUrl) {
        this.recoverUrl = recoverUrl;
    }

}
