package com.ourpos.api.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("classpath:config.properties")
@Service
public class SmsService {

    @Value("${sms.api.from}")
    private String from;

    private final DefaultMessageService messageService;

    public SmsService(@Value("${sms.api.key}") String apiKey, @Value("${sms.api.secret}") String apiSecret) {
        this.messageService = NurigoApp.INSTANCE.initialize(
            apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void sendOne(String to, String text) {
        Message message = new Message();

        message.setFrom(from);
        message.setTo(to);
        message.setText(text);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("SMS sent: {}", response);
    }
}
