package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.model.*;
import com.bolotov.oraclebot.repository.PurchaseRepository;
import com.bolotov.oraclebot.service.OracleService;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMenu;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.List;

@TelegramController
public class StartController {

    @Autowired
    private UserService userService;

    @Autowired
    TelegramMessageFactory messageFactory;

    @Autowired
    OracleService oracleService;

    @Autowired
    PurchaseRepository purchaseRepository;

    @TelegramAction(action="/start")
    public String start(TelegramEvent event) {

        User currentUser = userService.getUser(event.getChatId());
        if(currentUser == null)
            currentUser = userService.registration(event);

        switch (currentUser.getSelectRole().getName()) {
            case USER -> {
                return "/start_user";
            }
            case EXPERT -> {
                return "/start_expert";
            }
            case ADMIN -> {
                return "/start_admin";
            }
            case OWNER -> {
                return "/start_owner";
            }
        }

        return null;
    }

    @TelegramAction(action="/start_expert")
    public String startExpert(TelegramEvent event) {
        User currentUser = userService.getUser(event.getChatId());
        TelegramMessageMenu messageMenu = messageFactory.newTelegramMessageMenu(event.getChatId());
        messageMenu.addItemMenu("Главное меню", "/start");
        if(currentUser.getRoles().size()>1)
            messageMenu.addItemMenu("Сменить роль", "/change_role");
//        messageMenu.addItemMenu("Настроить отображение медиа", "/src_groups");
//        messageMenu.addItemMenu("Пополнить баланс", "/upbalance");

        TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event, getExpertInformation(currentUser));
        telegramMessage.addLayoutButton(2,1);
        telegramMessage.addButton("Свободные заказы", "/all_purchase");

        try {
            messageMenu.send();
            telegramMessage.send();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TelegramAction(action="/start_user")
    public String startUser(TelegramEvent event) {
        User currentUser = userService.getUser(event.getChatId());
        TelegramMessageMenu messageMenu = messageFactory.newTelegramMessageMenu(event.getChatId());
        messageMenu.addItemMenu("Главное меню", "/start");
        messageMenu.addItemMenu("Пополнить баланс", "/upbalance");
        if(currentUser.getRoles().size()>1)
            messageMenu.addItemMenu("Сменить роль", "/change_role");
        List<OracleCategory> categoryList = oracleService.getCategoriesByParent(null);

        List<String> names = categoryList.stream().map(l-> "⭐<i>" + l.getName() + "</i>").toList();
        String listService = String.join("\n", names);
        String helloText = String.format("" +
                        "\uD83D\uDC4B @%s\n\n" +
                        "Добро пожаловать в <i><b>Оракул</b></i>, наши услуги:\n" +
                        "%s\n\n",
                        currentUser.getUsername(),
                        listService );
        TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event, helloText + getUserInformation(currentUser));
        telegramMessage.addLayoutButton(2,1);
        telegramMessage.addButton("\uD83C\uDFAA\uD83C\uDFC3\u200D♀️\uD83C\uDFC3\uD83C\uDFFB\uD83C\uDFC3\uD83C\uDFFC\u200D♂️", "/menuUser");
        telegramMessage.addButton("Карта дня✨", "/view_group");

        try {
            messageMenu.send();
            telegramMessage.send();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TelegramAction(action="/menuUser")
    public String menuUser(TelegramEvent event) {
        try {
            User currentUser = userService.getUser(event.getChatId());
            List<OracleCategory> categoryList = oracleService.getCategoriesByParent(null);
            String menuText = "\uD83C\uDFAA\uD83E\uDDCD\uD83C\uDFFB\uD83E\uDDCD\u200D♂️\n\n";
            TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event,menuText + getUserInformation(currentUser));
            telegramMessage.addButton("Специалисты", "/experts");
            for(OracleCategory category : categoryList) {
                telegramMessage.addButton( "⭐" + category.getName(), "/view_group?category=" + category.getId());
            }
            telegramMessage.addButton( "Назад", "/start");
            telegramMessage.send();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getExpertInformation(User user) {
        List<OracleCategory> categoryList = oracleService.getCategoriesByParent(null);
        List<String> names = categoryList.stream().map(l-> "⭐<i>" + l.getName() + "</i>").toList();
        String text = "";
        Balance balance = user.getBalance();
        double amount = balance!=null ?  balance.getAmount() : 0;
        List<Purchase> selectedPurchase = purchaseRepository.findStatePurchaseByUser(user, Purchase.STATE.SELECTED);
        int countFreePurchase = purchaseRepository.countPurchaseByState(Purchase.STATE.WAIT_ANSWER);
        text = String.format("" +
                        "\uD83D\uDC4B @%s\n\n" +
                        "\uD83D\uDCB0 Баланс:  <code>%.2f\u20BD</code>\n" +
                        "\uD83E\uDD1D Доступно заказов:  <code>%d</code>\n" +
                        "\uD83E\uDD1D Заказы в работе:  <code>%d</code>\n" +
                user.getUsername(),
                amount,
                countFreePurchase,
                selectedPurchase.size());
        return text;
    }

    public String getUserInformation(User user) {
        List<OracleCategory> categoryList = oracleService.getCategoriesByParent(null);
        List<String> names = categoryList.stream().map(l-> "⭐<i>" + l.getName() + "</i>").toList();
        String listService = String.join("\n", names);
        String text = "";
        Balance balance = user.getBalance();
        double amount = balance!=null ?  balance.getAmount() : 0;
        int countPurchase = purchaseRepository.countStatePurchaseByCustomer(user, Purchase.STATE.WAIT_ANSWER);
        text = String.format(
                "\uD83D\uDCB0 Баланс:  <code>%.2f \u20BD</code>\n" +
                "\uD83E\uDD1D Заказы в работе:  <code>%d</code>\n" +
                "\uD83D\uDC64 Зарегистрирован: %s\n",
                amount,
                countPurchase,
                String.format("%1$TD", user.getDateRegistration()));
        return text;
    }
}
