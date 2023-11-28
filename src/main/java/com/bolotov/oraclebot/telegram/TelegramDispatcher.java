package com.bolotov.oraclebot.telegram;

import com.bolotov.oraclebot.telegram.annotation.TelegramAction;
import com.bolotov.oraclebot.telegram.annotation.TelegramController;
import com.bolotov.oraclebot.model.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TelegramDispatcher {

    private List<AccessRule> accessRuleList;

    @Autowired
    private ApplicationContext applicationContext;

    public void dispatch(TelegramEvent event) {
        String action = event.getActionName();

        Map<String, Object> controllers =  applicationContext.getBeansWithAnnotation(TelegramController.class);
        for(Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object controller = entry.getValue();
            for(Method method : controller.getClass().getDeclaredMethods()) {
                TelegramAction annotation = method.getAnnotation(TelegramAction.class);
                if(annotation == null)
                    continue;
                String actionMethod = annotation.action();
                if(action.equals(actionMethod)){
                    try {
                        String redirect = (String) method.invoke(controller, event);
                        if(redirect != null) {
                            event.setActionName(redirect);
                            dispatch(event);
                        }
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
        accessRuleList = new ArrayList<>();
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
