package com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCartController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCart.AbandonedCart;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.AbandonedCartService.AbandonedCartService;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.Interval.Interval;
import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.AbandonedCartHandler.SentMessage.SentMessage;

@CrossOrigin
@RestController
@RequestMapping("/abandoned")
public class AbandonedCartController {

    @Autowired
    private AbandonedCartService abandonedCartService;

    @GetMapping("/")
    public String helloTest() {
        return "Hello";
    }

    @PostConstruct
    public boolean initialize() {
        long[] intervals = { 60000, 120000 };
        abandonedCartService.setIntervals(intervals);
        abandonedCartService.startTimer();
        return true;
    }

    @PostMapping(value = "/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody JsonNode jsonNode) {
        String url = jsonNode.get("abandoned_checkout_url").asText();
        Long cartId = jsonNode.get("cart_token").asLong();
        JsonNode itemsArr = jsonNode.get("line_items");
        List<String> items = new ArrayList<>();
        if (itemsArr.isArray()) {
            for (JsonNode item : itemsArr) {
                String sku = item.get("sku").asText();
                int quantity = item.get("quantity").asInt();
                items.add(sku + "x" + quantity);
            }
        } else {
            if (!itemsArr.isNull()) {
                JsonNode item = itemsArr;
                String sku = item.get("sku").asText();
                int quantity = item.get("quantity").asInt();
                items.add(sku + "x" + quantity);
            }
        }
        JsonNode customer = jsonNode.get("customer");
        String firstName = customer.get("first_name").asText();
        String lastName = customer.get("last_name").asText();
        String customerName = firstName + " " + lastName;
        String customerEmail = customer.get("email").asText();
        URL recoverUrl = null;
        try {
            recoverUrl = new URL(url);
        } catch (MalformedURLException e) {
            ResponseEntity.badRequest();
        }
        Date abandonedTime = new Date();
        AbandonedCart cart = new AbandonedCart(cartId, items, customerName, customerEmail, recoverUrl, abandonedTime);
        abandonedCartService.addAbandoned(cart);
        return ResponseEntity.ok("Subscribed to complete cart notification");
    }

    @PostMapping(value = "/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody JsonNode jsonNode) {
        JsonNode orderNode = jsonNode.get("order");
        Long cartId = orderNode.get("cart_token").asLong();
        boolean res = abandonedCartService.removeAbandoned(cartId);
        if (!res)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscription does not exist");
        return ResponseEntity.ok("Successfully unsubscribed from notification");
    }

    @GetMapping(value = "/allmessages")
    public List<SentMessage> getAllMessages() {
        return abandonedCartService.showAllSent();
    }

    @GetMapping(value = "/getschedule")
    public long[] getSchedule() {
        return abandonedCartService.getIntervals();
    }

    @PostMapping(value = "/setschedule")
    public boolean setSchedule(@RequestBody List<Interval> intervals) {
        try {
            long[] intervalsInMs = new long[intervals.size()];
            int count = 0;
            for (Interval interval : intervals) {
                intervalsInMs[count++] = interval.intervalTimeInMs();
            }
            abandonedCartService.setIntervals(intervalsInMs);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
