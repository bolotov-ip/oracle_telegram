package com.bolotov.oraclebot.config;

import com.bolotov.oraclebot.annotation.TelegramAction;
import com.bolotov.oraclebot.annotation.TelegramController;
import com.bolotov.oraclebot.model.Role;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.telegram.TelegramEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DispatcherController {

    private List<AccessRule> accessRuleList;

    @Autowired
    private ApplicationContext applicationContext;

    public void dispatch(Update update) {
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
                        method.invoke(controller, event);
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


    @PostConstruct
    public void configureAccess() {
        accessRuleList.add(new AccessRule("/user*").hasRole("USER"));
    }

    public static class AccessRule {

        private List<String> roleList = new ArrayList<>();
        private String path;

        public AccessRule(String path) {
            this.path = path;
        }

        public AccessRule hasRole(String role) {
            roleList.add(role);
            return this;
        }

        public boolean access(String currentPath, Role currentRole) {
            if(roleList.stream().filter(r->r.equals(currentRole.getName())).count() == 0)
                return false;
            if(path.endsWith("*")) {
                String startPath = path.substring(0, path.length() -1);
                if(currentPath.startsWith(startPath)) {
                    return true;
                }
            }
            else {
                if(path.equals(currentPath))
                    return true;
            }
            return false;
        }
    }
}
