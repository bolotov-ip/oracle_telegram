package com.bolotov.oraclebot.telegram;

import java.io.File;

public interface TelegramMessageMedia extends BasicMessage {

    public TelegramMessageMedia addPhotoTelegramId(String telegramId);

    public TelegramMessageMedia addPhotoFile(File file);

    public TelegramMessageMedia addVideoTelegramId(String telegramId);

    public TelegramMessageMedia addVideoFile(File file);

}
