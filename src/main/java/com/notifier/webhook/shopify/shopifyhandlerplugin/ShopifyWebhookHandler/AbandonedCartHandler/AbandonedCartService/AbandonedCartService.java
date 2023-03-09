package com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCartService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCart.AbandonedCart;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCartService.pqEntry.pqEntry;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.SentMessage.SentMessage;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.EmailService.EmailDetails.EmailDetails;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.EmailService.EmailServiceInterface.EmailServiceInterface;

@Service
public class AbandonedCartService {

    @Autowired
    private EmailServiceInterface emailServiceInterface;

    private class ScheduledTask extends TimerTask {
        private boolean isDue(pqEntry curr, Date timeNow) {
            AbandonedCart currCart = curr.getAbandonedCart();
            Date currTime = currCart.getAbandonedTime();
            return timeNow.compareTo(currTime) >= 0;
        }

        @Override
        public void run() {
            Date timeNow = new Date();
            while (!scheduledTasks.isEmpty()) {
                pqEntry current;
                synchronized (this) {
                    if (!isDue(scheduledTasks.peek(), timeNow))
                        return;
                    current = scheduledTasks.poll();
                }
                AbandonedCart currCart = current.getAbandonedCart();
                if (abandonedCartIds.contains(currCart.getCartId())) {
                    if (current.getSchedulerLevel() > 0) {
                        // Send email logic
                        final String recipient = currCart.getCustomerEmail();
                        final String msgBody = String.format(
                                "Hi %s, \n Please complete your checkout using the following url %s. \nCheers!",
                                currCart.getCustomer(), currCart.getRecoverUrl().toString());
                        EmailDetails email = new EmailDetails(recipient, msgBody, "Your cart needs your attention!",
                                null);
                        String message = emailServiceInterface.sendSimpleMail(email);
                        System.out.println(message + " to " + currCart.getCustomerEmail());
                        sentMessages.add(new SentMessage(currCart.getCustomerEmail(), timeNow, currCart.getItems(),
                                currCart.getRecoverUrl()));
                    }
                    if (current.getSchedulerLevel() < intervals.length) {
                        long timeInMs = currCart.getAbandonedStartInMs();
                        currCart.setAbandonedTime(new Date(timeInMs + intervals[current.getSchedulerLevel()]));
                        current.setSchedulerLevel(current.getSchedulerLevel() + 1);
                        synchronized (this) {
                            scheduledTasks.offer(current);
                        }
                    }
                }
            }
        }
    }

    private long[] intervals;
    private PriorityQueue<pqEntry> scheduledTasks;
    private List<SentMessage> sentMessages;
    private Set<Long> abandonedCartIds;

    public AbandonedCartService(long[] intervals) {
        this.intervals = intervals;
        this.abandonedCartIds = new HashSet<>();
        this.sentMessages = new ArrayList<>();
        this.scheduledTasks = new PriorityQueue<>();

    }

    public void startTimer() {
        Timer scheduler = new Timer();
        TimerTask checkEveryMinute = new ScheduledTask();
        scheduler.scheduleAtFixedRate(checkEveryMinute, 0, 60 * 1000);
    }

    public void addAbandoned(AbandonedCart newCart) {
        abandonedCartIds.add(newCart.getCartId());
        synchronized (this) {
            scheduledTasks.offer(new pqEntry(newCart, 0));
        }
    }

    public boolean removeAbandoned(long cartId) {
        return abandonedCartIds.remove(cartId);
    }

    public long[] getIntervals() {
        return intervals;
    }

    public void setIntervals(long[] intervals) {
        this.intervals = intervals;
    }

    public List<SentMessage> showAllSent() {
        return sentMessages;
    }

}
