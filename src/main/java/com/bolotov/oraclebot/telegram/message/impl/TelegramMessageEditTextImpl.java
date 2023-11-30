package com.bolotov.oraclebot.telegram.message.impl;

import com.bolotov.oraclebot.telegram.message.TelegramMessageEditText;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramMessageEditTextImpl extends TelegramMessageTextImpl implements TelegramMessageEditText {
    private int messageId;
    @Override
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public void send() throws TelegramApiException {
        try {
            EditMessageText  editMessageText= new EditMessageText();
            editMessageText.enableHtml(true);
            editMessageText.setText(text);
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            setButtons(editMessageText);
            if(buttons.size()>countElementsInPage || currentPageNumber>1)
                addNavigateKeyboard(editMessageText);
            bot.execute(editMessageText);
        }
        catch (Exception e) {
            throw new TelegramApiException(e);
        }
    }
}
