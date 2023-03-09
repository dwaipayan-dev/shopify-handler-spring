package com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.Interval;

public class Interval {
    private int days;
    private int hours;
    private int minutes;

    public long intervalTimeInMs() {
        long res = 0;
        res += days * 24 * 60 * 60 * 1000;
        res += hours * 60 * 60 * 1000;
        res += minutes * 60 * 1000;
        return res;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Interval(int days, int hours, int minutes) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }

}
