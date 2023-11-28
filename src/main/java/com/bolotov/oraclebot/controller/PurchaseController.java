package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.exception.OracleServiceException;
import com.bolotov.oraclebot.model.*;
import com.bolotov.oraclebot.service.OracleDataService;
import com.bolotov.oraclebot.service.OracleService;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMedia;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.Set;

@TelegramController
public class PurchaseController {

    @Autowired
    OracleService oracleService;

    @Autowired
    OracleDataService oracleDataService;

    @Autowired
    UserService userService;

    @Autowired
    TelegramMessageFactory messageFactory;

    @TelegramAction(action="/purchase")
    public String purchase(TelegramEvent event) {
        try {
            Long oracleId = Long.valueOf(event.getValues().get("oracle"));
            Oracle oracle = oracleService.getOracleById(oracleId);
            User customer = userService.getUser(event.getChatId());
            oracleService.purchase(customer, oracle);
            TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event, "Успех!!!\nОжидайте оповещения о выполнении заказа");
            telegramMessage.addButton( "Назад", "/start");
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("" + e.getMessage());
            return "/error";
        }
        return null;
    }

    @TelegramAction(action="/all_purchase")
    public String viewAllPurchase(TelegramEvent event) {
        try {

            User user = userService.getUser(event.getChatId());
            List<Purchase> selectedPurchases = oracleService.getSelectedPurchase(user);
            for(Purchase p : selectedPurchases) {
                event.getValues().put("id_p", String.valueOf(p.getId()));
                return "/view_purchase";
            }
            List<Purchase> purchaseList = oracleService.getAllFreePurchase();
            TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event, "Все невыполненные заказы");

            for(Purchase purchase : purchaseList) {
                Oracle oracle = oracleService.getOracleById(purchase.getOracleId());
                if(oracle == null) {
                    throw new OracleServiceException(String.format("Для заказа с %d отсутствует предсказание %d. Обратитесь к администратору",
                            purchase.getId(), purchase.getOracleId()));
                }
                String nameOracle = oracle.getCategory().getHierarchy() + "." + oracle.getName();
                telegramMessage.addButton(String.format("%s|%s|%.2f", purchase.getCustomer().getUsername(), nameOracle, purchase.getPrice()),
                        "/view_purchase?id_p="+purchase.getId());
            }
            telegramMessage.addButton( "Назад", "/start");
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("" + e.getMessage());
            return "/error";
        }
        return null;
    }


    @TelegramAction(action="/view_purchase")
    public String viewPurchase(TelegramEvent event) {
        try {
            User user = userService.getUser(event.getChatId());
            Purchase purchase = oracleService.selectPurchaseForAnswer(user, Long.valueOf(event.getValues().get("id_p")));
            Oracle oracle = oracleService.getOracleById(purchase.getOracleId());
            if(oracle==null){
                event.setText(String.format("Для заказа №%s удалено предсказание. Обратитесь к администратору", purchase.getId() ));
                return "/error";
            }
            String nameOracle = oracle.getCategory().getHierarchy() + "." + oracle.getName();
            String text = String.format(
                            "Заказ №%d выбран для ответа\n" +
                            "Заказчик: %s\n" +
                            "Товар: %s\n" +
                            "Цена: %.2f \u20BD \n" +
                            "Дата заказа: %s\n\n" +
                            "Также в качестве ответа вы можете отправить фото, или видео.\n" +
                            "Просто отправьте его сразу в чат прямо сейчас",
                            purchase.getId(),
                            purchase.getCustomer().getUsername(),
                            nameOracle,
                            purchase.getPrice(),
                            String.format("%1$TD", purchase.getDatePurchase())
            );
            TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event, text);
            for(Map.Entry<String, String> entry : oracle.getOracleResult().entrySet()) {
                telegramMessage.addButton( entry.getKey(), String.format("/answer?id_p=%s,name=%s", purchase.getId(), entry.getKey()));
            }
            telegramMessage.addButton( "Отказаться", String.format("/unselect_purchase?id_p=%d", purchase.getId()));
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("" + e.getMessage());
            return "/error";
        }
        return null;
    }

    @TelegramAction(action="/unselect_purchase")
    public String unselectPurchase(TelegramEvent event) {
        try {
            oracleService.unselectPurchase(Long.valueOf(event.getValues().get("id_p")));
        }
        catch (Exception e) {
            event.setText("Обратитесь к администратору: " + e.getMessage());
            return "/error";
        }

        return "/all_purchase";
    }

    @TelegramAction(action="/answer")
    public String answer(TelegramEvent event) {
        try {
            Long purchaseId = Long.valueOf(event.getValues().get("id_p"));
            Purchase purchase = oracleService.getPurchaseById(purchaseId);
            String resultName = event.getValues().get("name");
            Oracle oracle = oracleService.getOracleById(purchase.getOracleId());
            String result = oracle.getOracleResult().get(resultName);
            String groupName = oracle.getSourceGroup();
            List<SourceSet> sourceSets = oracleDataService.getSourceSetByGroup(groupName);
            User user = userService.getUser(event.getChatId());
            String selectSourceSetName = user.getSelectSources().get(groupName);
            SourceSet selectSet = getSelectSourceSet(sourceSets, selectSourceSetName);
            Source source = getSelectSource(selectSet.getSources(), resultName);
            if(source!=null) {
                TelegramMessageMedia messageMediaAnswer = messageFactory.newTelegramMessageMedia(purchase.getCustomer().getChatId(), "");
                switch (source.getType()){
                    case PHOTO -> messageMediaAnswer.addPhotoTelegramId(source.getTelegramId());
                    case VIDEO -> messageMediaAnswer.addVideoTelegramId(source.getTelegramId());
                }
                messageMediaAnswer.send();
            }
            TelegramMessageText telegramMessageAnswer = messageFactory.newTelegramMessageText(event, result);
            telegramMessageAnswer.setChatId(purchase.getCustomer().getChatId());
            telegramMessageAnswer.send();
            oracleService.donePurchase(purchase);
            TelegramMessageText telegramMessage = messageFactory.newTelegramAdaptiveMessageText(event, String.format("Заказ №%d успешно исполнен", purchase.getId()));
            telegramMessage.addButton("Выбрать новый заказ", "/all_purchase");
            telegramMessage.send();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private SourceSet getSelectSourceSet(List<SourceSet> sourceSets, String name) {
        for(SourceSet set : sourceSets) {
            if(set.getName().equals(name))
                return set;
        }
        return null;
    }

    private Source getSelectSource(Set<Source> sources, String name) {
        for(Source source : sources) {
            if(source.getName().equals(name))
                return source;
        }
        return null;
    }
}
