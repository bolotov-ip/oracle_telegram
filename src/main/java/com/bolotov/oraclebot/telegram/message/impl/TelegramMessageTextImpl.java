package com.bolotov.oraclebot.telegram.message.impl;

import com.bolotov.oraclebot.telegram.TelegramButton;
import com.bolotov.oraclebot.telegram.message.TelegramMessage;
import com.bolotov.oraclebot.telegram.TelegramBot;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TelegramMessageTextImpl extends TelegramMessageImpl implements TelegramMessageText {

    protected List<TelegramButton> buttons = new ArrayList<>();

    protected int countElementsInPage = 10;

    protected int[] layoutButton;

    protected int currentPageNumber = 1;

    protected String action;

    @Override
    public void send() throws TelegramApiException {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
            setButtons(sendMessage);
            if(countElementsInPage>buttons.size())
                addNavigateKeyboard(sendMessage);
            bot.execute(sendMessage);
        }
        catch (Exception e) {
            throw new TelegramApiException(e);
        }
    }

    @Override
    public TelegramMessageText addButton(String name, String action) {
        TelegramButton button = new TelegramButton();
        button.setText(name);
        button.setAction(action);
        buttons.add(button);
        return this;
    }

    @Override
    public TelegramMessageText addLayoutButton(int... args) {
        layoutButton = args;
        return this;
    }

    @Override
    public TelegramMessageText setPageSize(int countElementInPage) {
        this.countElementsInPage = countElementInPage;
        return this;
    }

    @Override
    public TelegramMessage setCurrentPage(int currentPage) {
        currentPageNumber = currentPage;
        return this;
    }

    @Override
    public TelegramMessage setAction(String action) {
        this.action = action;
        return this;
    }

    protected void addNavigateKeyboard(BotApiMethod<?> msg) throws IOException {
        int countAllElements = buttons.size();
        int countPage = (int) Math.ceil(Double.valueOf(countAllElements)/countElementsInPage);

        ReplyKeyboard replyKeyboard = msg instanceof SendMessage ? ((SendMessage)msg).getReplyMarkup() : ((EditMessageText)msg).getReplyMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = ((InlineKeyboardMarkup) replyKeyboard).getKeyboard();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        if(currentPageNumber>1) {
            TelegramButton button = new TelegramButton();
            button.setAction(action);
            button.addValue("numberPage", String.valueOf(currentPageNumber-1 ));
            InlineKeyboardButton buttonPrev = new InlineKeyboardButton();
            buttonPrev.setText("<");
            buttonPrev.setCallbackData(button.toCompressCallbackString());
            rowInLine.add(buttonPrev);
        }
        if(currentPageNumber>1 || currentPageNumber<countPage) {
            InlineKeyboardButton btnInfo = new InlineKeyboardButton();
            btnInfo.setText(currentPageNumber + " - " + countPage);
            btnInfo.setCallbackData("empty");
            rowInLine.add(btnInfo);
        }
        if(currentPageNumber<countPage) {
            TelegramButton button = new TelegramButton();
            button.setAction(action);
            button.addValue("numberPage", String.valueOf(currentPageNumber+1 ));
            InlineKeyboardButton buttonNext = new InlineKeyboardButton();
            buttonNext.setText(">");
            buttonNext.setCallbackData(button.toCompressCallbackString());
            rowInLine.add(buttonNext);
        }
        rowsInLine.add(rowInLine);
        ((InlineKeyboardMarkup) replyKeyboard).setKeyboard(rowsInLine);
        replyMarkup((InlineKeyboardMarkup) replyKeyboard, msg);
    }

    protected void setButtons(BotApiMethod<?> msg) throws IOException {
        if(buttons==null || buttons.size()==0)
            return;
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        int countAddRowBtn = 0;
        int rowCounter = 0;
        int column = 1;
        for(TelegramButton btn : buttons) {
            if(layoutButton!=null && rowCounter<layoutButton.length)
                column = layoutButton[rowCounter];
            if(countAddRowBtn<column){
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(btn.getText());
                button.setCallbackData(btn.toCompressCallbackString());
                rowInLine.add(button);
                countAddRowBtn++;
            }
            else {
                rowsInLine.add(rowInLine);
                rowInLine = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(btn.getText());
                button.setCallbackData(btn.toCompressCallbackString());
                rowInLine.add(button);
                countAddRowBtn=1;
            }
        }
        if(!rowsInLine.contains(rowInLine))
            rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        replyMarkup(markupInLine, msg);
    }

    protected void replyMarkup(InlineKeyboardMarkup markupInLine, BotApiMethod<?> msg) {
        if(msg instanceof SendMessage)
            ((SendMessage)msg).setReplyMarkup(markupInLine);
        else if(msg instanceof EditMessageText) {
            ((EditMessageText)msg).setReplyMarkup(markupInLine);
        }
    }
}