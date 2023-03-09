package com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.EmailService.EmailServiceInterface;

import com.notifier.webhook.shopify.shopifyhandlerplugin.ShopifyWebhookHandler.EmailService.EmailDetails.EmailDetails;

public interface EmailServiceInterface {
    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}
