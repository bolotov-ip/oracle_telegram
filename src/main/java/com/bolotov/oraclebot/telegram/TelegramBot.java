package com.bolotov.oraclebot.telegram;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Autowired
    private DispatcherController dispatcherController;

    public TelegramBot(@Value("${bot.token}")String token) {
        super(token);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        TelegramEvent event = TelegramEvent.valueOf(update);
        dispatcherController.dispatch(event);
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
