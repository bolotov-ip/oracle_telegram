package com.bolotov.oraclebot.telegram.message;

import java.io.File;

public interface TelegramMessageMedia extends TelegramMessage {

    public TelegramMessageMedia addPhotoTelegramId(String telegramId);

    public TelegramMessageMedia addPhotoFile(File file);

    public TelegramMessageMedia addVideoTelegramId(String telegramId);

    public TelegramMessageMedia addVideoFile(File file);

}
