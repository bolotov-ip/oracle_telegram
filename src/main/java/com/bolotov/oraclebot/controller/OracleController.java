package com.bolotov.oraclebot.controller;

import com.bolotov.oraclebot.model.Oracle;
import com.bolotov.oraclebot.model.OracleCategory;
import com.bolotov.oraclebot.service.OracleDataService;
import com.bolotov.oraclebot.service.OracleService;
import com.bolotov.oraclebot.service.UserService;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageText;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@TelegramController
public class OracleController {

    @Autowired
    OracleService oracleService;

    @Autowired
    TelegramMessageFactory messageFactory;

    @Autowired
    private UserService userService;

    @TelegramAction(action="/view_group")
    public String viewGroup(TelegramEvent event) {
        try {
            Long parentCategoryId = event.getValues().get("category")!=null?Long.valueOf(event.getValues().get("category")):null;
            OracleCategory parentCategory = oracleService.getCategoryById(parentCategoryId);
            List<OracleCategory> categoryList = oracleService.getCategoriesByParent(parentCategoryId);

            TelegramMessageText telegramMessage = messageFactory.newTelegramMessageText(event, "Здесь представлены все предлагаемые возможности");
            for(OracleCategory category : categoryList) {
                telegramMessage.addButton( category.getName(), "/view_group?category=" + category.getId());
            }
            if(parentCategory!=null) {
                for(Oracle oracle : parentCategory.getOracles()) {
                    telegramMessage.addButton( oracle.getName(), "/view_oracle?oracle=" + oracle.getId());
                }
            }
            telegramMessage.addButton( "Назад", "/start");
            telegramMessage.send();
        } catch (Exception e) {
            event.setText("Не удалось добавить ресурс.\nТекст ошибки:\n" + e.getMessage());
            return "/error";
        }
        return null;
    }
}
