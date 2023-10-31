package com.bolotov.oraclebot.telegram;

import com.bolotov.oraclebot.config.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TelegramMessage {



    private static TelegramBot bot;

    public static void setBot(TelegramBot bot) {
        TelegramMessage.bot = bot;
    }

    public TelegramMessage(String textMessage, Long chatId, Integer messageId, String action) {
        this.text = textMessage;
        this.chatId = chatId;
        this.messageId = messageId;
        this.action = action;
    }

    public static TelegramMessage valueOf(String text, TelegramEvent event) {
        TelegramMessage message = new TelegramMessage(text, event.getChatId(), event.getMessageId(), event.getActionName());
        return message;
    }

    private String action;

    private String text;

    private Long chatId;

    private Integer messageId;

    private List<Source> sources;

    private List<TelegramButton> buttons = new ArrayList<>();

    private int[] layoutButton;

    private int currentPageNumber;

    private int countAllElements;

    private int countElementsInPage;

    private void addNavigateKeyboard(BotApiMethod<?> msg) throws IOException {

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

    private void setButtons(BotApiMethod<?> msg) throws IOException {
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

    private void replyMarkup(InlineKeyboardMarkup markupInLine, BotApiMethod<?> msg) {
        if(msg instanceof SendMessage)
            ((SendMessage)msg).setReplyMarkup(markupInLine);
        else if(msg instanceof EditMessageText) {
            ((EditMessageText)msg).setReplyMarkup(markupInLine);
        }
    }

    public void send() {
        try{
            getSendMediaGroupList().stream().forEach(mediaGroup -> {
                try {
                    bot.execute(mediaGroup);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });

            SendMessage msg = messageId == null ? new SendMessage() : null;
            if(msg !=null) {
                msg.setText(text);
                msg.setChatId(chatId);
                setButtons(msg);
                if(currentPageNumber!=0 && countAllElements!=0 && countElementsInPage!=0)
                    addNavigateKeyboard(msg);
                try {
                    bot.execute(msg);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            EditMessageText editMessageText = messageId == null ? null : new EditMessageText();
            if(editMessageText != null) {
                editMessageText.setMessageId(messageId);
                editMessageText.setChatId(chatId);
                editMessageText.setText(text);
                setButtons(editMessageText);
                if(currentPageNumber!=0 && countAllElements!=0 && countElementsInPage!=0)
                    addNavigateKeyboard(editMessageText);
                try {
                    bot.execute(editMessageText);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<SendMediaGroup> getSendMediaGroupList() {
        List<SendMediaGroup> sendMediaGroupList = new ArrayList<>();
        if(sources != null && sources.size()>0) {
            SendMediaGroup sendMediaGroup = new SendMediaGroup();
            sendMediaGroup.setChatId(chatId);
            List<InputMedia> inputMedia = new ArrayList<>();
            int counter = 1;
            for(Source source : sources) {
                if(source.getTypeSource().equals(Source.TYPE_SOURCE.PHOTO)) {
                    if(source.getTypePath().equals(Source.TYPE_PATH.TELEGRAM)) {
                        inputMedia.add(new InputMediaPhoto(source.getPath()));
                    }
                    else if(source.getTypePath().equals(Source.TYPE_PATH.PATH)) {
                        InputMedia media = new InputMediaPhoto();
                        media.setMedia(new File(source.getPath()), UUID.randomUUID().toString());
                        inputMedia.add(media);
                    }
                    counter++;
                }
                if(source.getTypeSource().equals(Source.TYPE_SOURCE.VIDEO)) {
                    if(source.getTypePath().equals(Source.TYPE_PATH.TELEGRAM)) {
                        inputMedia.add(new InputMediaVideo(source.getPath()));
                    }
                    else if(source.getTypePath().equals(Source.TYPE_PATH.PATH)) {
                        InputMedia media = new InputMediaVideo();
                        media.setMedia(new File(source.getPath()), UUID.randomUUID().toString());
                        inputMedia.add(media);
                    }
                    counter++;
                }
                if(counter==10) {
                    sendMediaGroupList.add(sendMediaGroup);
                    sendMediaGroup = new SendMediaGroup();
                }
            }
        }
        return sendMediaGroupList;
    }

    public TelegramButton addButton(String action, String text) {
        TelegramButton button = new TelegramButton();
        button.setText(text);
        button.setAction(action);
        buttons.add(button);
        return button;
    }

    public TelegramMessage setButtonLayout(int ...args) {
        layoutButton = args;
        return this;
    }

    public TelegramMessage addPhotoByTgId(String telegramId) {
        Source source = new Source(Source.TYPE_SOURCE.PHOTO, Source.TYPE_PATH.TELEGRAM, telegramId);
        sources.add(source);
        return this;
    }

    public TelegramMessage addPhotoByTgLocalPath(String localPath) {
        Source source = new Source(Source.TYPE_SOURCE.PHOTO, Source.TYPE_PATH.PATH, localPath);
        sources.add(source);
        return this;
    }

    public TelegramMessage addVideoByTgId(String telegramId) {
        Source source = new Source(Source.TYPE_SOURCE.VIDEO, Source.TYPE_PATH.TELEGRAM, telegramId);
        sources.add(source);
        return this;
    }

    public TelegramMessage addVideoByTgLocalPath(String localPath) {
        Source source = new Source(Source.TYPE_SOURCE.VIDEO, Source.TYPE_PATH.PATH, localPath);
        sources.add(source);
        return this;
    }

    private class Source {

        public enum TYPE_SOURCE {
            PHOTO, VIDEO;
        }

        public enum TYPE_PATH {
            TELEGRAM, URL, PATH;
        }

        private TYPE_SOURCE typeSource;
        private TYPE_PATH typePath;
        private String path;

        public Source(TYPE_SOURCE typeSource, TYPE_PATH typePath, String path) {
            this.typeSource = typeSource;
            this.typePath = typePath;
            this.path = path;
        }

        public TYPE_SOURCE getTypeSource() {
            return typeSource;
        }

        public void setTypeSource(TYPE_SOURCE typeSource) {
            this.typeSource = typeSource;
        }

        public TYPE_PATH getTypePath() {
            return typePath;
        }

        public void setTypePath(TYPE_PATH typePath) {
            this.typePath = typePath;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
