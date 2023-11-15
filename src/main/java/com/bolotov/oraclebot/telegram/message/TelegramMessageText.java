package com.bolotov.oraclebot.telegram.message;

public interface TelegramMessageText extends TelegramMessage {

    public TelegramMessage addButton(String name, String action);

    public TelegramMessage addLayoutButton(int ...args);

    public TelegramMessage setPageSize(int countElementInPage);

    public TelegramMessage setCurrentPage(Integer currentPage);

    public TelegramMessage setAction(String action);

    public TelegramMessage setFullAction(String action);
}
