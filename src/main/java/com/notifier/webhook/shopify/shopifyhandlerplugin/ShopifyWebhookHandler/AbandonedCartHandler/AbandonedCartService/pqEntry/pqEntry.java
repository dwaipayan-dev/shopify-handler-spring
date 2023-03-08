package com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCartService.pqEntry;

import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCart.AbandonedCart;

public class pqEntry implements Comparable<pqEntry> {
    private AbandonedCart abandonedCart;
    private int schedulerLevel;

    public pqEntry(AbandonedCart abandonedCart, int schedulerLevel) {
        this.abandonedCart = abandonedCart;
        this.schedulerLevel = schedulerLevel;
    }

    public AbandonedCart getAbandonedCart() {
        return abandonedCart;
    }

    public void setAbandonedCart(AbandonedCart abandonedCart) {
        this.abandonedCart = abandonedCart;
    }

    public int getSchedulerLevel() {
        return schedulerLevel;
    }

    public void setSchedulerLevel(int schedulerLevel) {
        this.schedulerLevel = schedulerLevel;
    }

    @Override
    public int compareTo(pqEntry o) {
        if (this.abandonedCart.getAbandonedTime().compareTo(o.abandonedCart.getAbandonedTime()) < 0)
            return -1;
        else if (this.abandonedCart.getAbandonedTime().compareTo(o.abandonedCart.getAbandonedTime()) > 0)
            return 1;
        else {
            return o.schedulerLevel - this.schedulerLevel;
        }
    }

}
