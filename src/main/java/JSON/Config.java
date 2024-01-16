package JSON;

public class Config {
    public String serviceToken;
    public String botToken;
    public long chatId;
    public long adminId;
    public Integer appId;
    public Integer requestLimitPerDay;
    public boolean dataFromConfigFile;
    public Integer telegramSymLimitPerMessage;

    public Config(String serviceToken, Integer appId, Integer requestLimitPerDay, String botToken, long chatId, Integer telegramSymLimitPerMessage, long adminId) {
        this.serviceToken = serviceToken;
        this.appId = appId;
        this.dataFromConfigFile = true;
        this.requestLimitPerDay = requestLimitPerDay;
        this.botToken = botToken;
        this.chatId = chatId;
        this.telegramSymLimitPerMessage = telegramSymLimitPerMessage;
        this.adminId = adminId;

    }

    public Config(){
        this.dataFromConfigFile = false;

    }
}
