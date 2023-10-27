package com.bolotov.oraclebot.telegram;

import com.bolotov.oraclebot.util.StringCompression;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TelegramButton {

    public static TelegramButton valueOf(String callbackText) throws IOException {
        TelegramButton button = new TelegramButton();
        String text = StringCompression.decompress(callbackText.getBytes());
        String[] data = text.split("/?");
        String action = data[0];
        Map<String, String> values = new HashMap<>();
        if(data.length>1) {
            String[] keyValues = data[1].split(",");
            for(String keyValue : keyValues) {
                values.put(keyValue.split("=")[0], keyValue.split("=")[1]);
            }
        }
        button.setAction(action);
        button.setValues(values);
        return new TelegramButton();
    }
    private String action;

    private Map<String, String> values;

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValue(String nameValue) {
        return values.get(nameValue);
    }
}
