package com.bolotov.oraclebot.telegram.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TelegramButton {

    public static TelegramButton valueOf(String callbackText) throws IOException {
        TelegramButton button = new TelegramButton();
//        String text = StringCompression.decompress(callbackText);
        String text = callbackText;
        String[] data = text.split("\\?");
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
        return button;
    }
    private String text;

    private String action;

    private Map<String, String> values = new HashMap<>();

    public Map<String, String> getValues() {
        return values;
    }

    public TelegramButton setValues(Map<String, String> values) {
        this.values = values;
        return this;
    }

    public String getAction() {
        return action;
    }

    public TelegramButton setAction(String action) {
        this.action = action;
        return this;
    }

    public String getValue(String nameValue) {
        return values.get(nameValue);
    }

    public TelegramButton addValue(String key, String value) {
        values.put(key, value);
        return this;
    }

    public String toCallbackString() throws IOException {
        String valuesString = "";
        for(Map.Entry<String, String> value : values.entrySet()) {
            valuesString = valuesString + String.format("%s=%s,", value.getKey(), value.getValue());
        }
        if(!valuesString.isEmpty()) {
            valuesString = valuesString.substring(0, valuesString.length()-1);
            valuesString = "?" + valuesString;
        }
        String callbackString = action + valuesString;
        return callbackString;
    }

    public String toCompressCallbackString() throws IOException {
//        return StringCompression.compress(toCallbackString());
        return toCallbackString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
