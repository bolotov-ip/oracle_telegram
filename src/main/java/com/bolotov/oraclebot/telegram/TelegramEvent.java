package com.bolotov.oraclebot.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TelegramEvent {

    public static TelegramEvent valueOf(Update update){
        TelegramEvent event = new TelegramEvent();
        try{
            String action = "";
            Long chatId = 0L;
            Integer messageId = null;
            Map<String, String> values = new HashMap<>();
            if(update.hasMessage()) {
                action = update.getMessage().getText();
                if(!action.startsWith("/")) {
                    String messageText = update.getMessage().getText();
                    action = messageText.substring(messageText.indexOf(":"));
                    String text = messageText.substring(messageText.indexOf(" ") + 1);
                    event.setText(text);
                }
                chatId = update.getMessage().getChatId();
            } else if (update.hasCallbackQuery()) {
                String callbackText = update.getCallbackQuery().getData();
                TelegramButton button = TelegramButton.valueOf(callbackText);
                action = button.getAction();
                chatId = update.getCallbackQuery().getMessage().getChatId();
                messageId = update.getCallbackQuery().getMessage().getMessageId();
                values = button.getValues();
            } else if (update.hasPreCheckoutQuery()) {

            }
            event.setActionName(action);
            event.setChatId(chatId);
            event.setValues(values);
            event.setMessageId(messageId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    private String actionName;

    private Long chatId;

    private Integer messageId;

    private Map<String, String> values;

    private String text;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
