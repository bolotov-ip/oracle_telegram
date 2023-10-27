package com.bolotov.oraclebot.config;

import com.bolotov.oraclebot.annotation.TelegramAction;
import com.bolotov.oraclebot.annotation.TelegramController;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class ControllerDistributor {

    @Autowired
    private ApplicationContext applicationContext;

    public void define(Update update) {
        TelegramEvent event = TelegramEvent.valueOf(update);
        String action = event.getActionName();

        Map<String, Object> controllers =  applicationContext.getBeansWithAnnotation(TelegramController.class);
        for(Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object controller = entry.getValue();
            for(Method method : controller.getClass().getDeclaredMethods()) {
                TelegramAction annotation = method.getAnnotation(TelegramAction.class);
                String actionMethod = annotation.action();
                if(action.equals(actionMethod)){
                    try {
                        method.invoke(controller, new TelegramEvent());
                        return;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
