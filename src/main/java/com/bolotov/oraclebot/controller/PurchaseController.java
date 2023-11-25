package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.exception.OracleServiceException;
import com.bolotov.oraclebot.model.Oracle;
import com.bolotov.oraclebot.model.Purchase;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.service.OracleService;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@TelegramController
public class PurchaseController {

    @Autowired
    OracleService oracleService;

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
            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Успех!!!\nОжидайте оповещения о выполнении заказа");
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
            List<Purchase> purchaseList = oracleService.getAllFreePurchase();
            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Все невыполненные заказы");

            for(Purchase purchase : purchaseList) {
                Oracle oracle = oracleService.getOracleById(purchase.getOracleId());
                if(oracle == null) {
                    throw new OracleServiceException(String.format("Для заказа с %d отсутствует предсказание %d. Обратитесь к администратору",
                            purchase.getId(), purchase.getOracleId()));
                }
                String nameOracle = oracle.getCategory().getHierarchy() + "." + oracle.getName();
                telegramMessage.addButton(String.format("%s|%s|%.2f", user.getUsername(), nameOracle, purchase.getPrice()),
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
            String nameOracle = oracle.getCategory().getHierarchy() + "." + oracle.getName();
            String text = String.format(
                            "Заказ №%d выбран для ответа\n" +
                            "Заказчик: %s\n" +
                            "Товар: %s\n" +
                            "Цена: %.2f\n" +
                            "Дата заказа: %s\n\n" +
                            "Также в качестве ответ вы можете отправить фото, или видео.\n" +
                            "Просто отправьте его сразу в чат прямо сейчас",
                            purchase.getId(),
                            user.getUsername(),
                            nameOracle,
                            purchase.getPrice(),
                            purchase.getDatePurchase()
            );
            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, text);
            for(Map.Entry<String, String> entry : oracle.getOracleResult().entrySet()) {
                telegramMessage.addButton( entry.getKey(), String.format("/answer?id_o=%s,name=%s", purchase.getId(), entry.getKey()));
            }
            telegramMessage.addButton( "Отказаться", String.format("/unselect_purchase?id_p=", purchase.getId()));
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("" + e.getMessage());
            return "/error";
        }
        return null;
    }

    @TelegramAction(action="/unselect_purchase")
    public String unselectPurchase(TelegramEvent event) {
        return null;
    }
}
