package ru.bykov.internettelegrambot.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bykov.internettelegrambot.service.InternetProviderClient;
import ru.bykov.internettelegrambot.service.TelegramBotHandler;

@Configuration
@NoArgsConstructor
public class ApplicationConfiguration {
    @Value("${external.api.base-url}")
    private String url;
    @Bean
    public InternetProviderClient internetProviderClient(){
        InternetProviderClient internetProviderClient = new InternetProviderClient();
        internetProviderClient.setRestTemplate(restTemplate());
        internetProviderClient.setUrl(url);
        return internetProviderClient;
    }
    @Bean
    public RestTemplate restTemplate(){return new RestTemplate();}
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotHandler telegramBotHandler) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramBotHandler);
        return telegramBotsApi;
    }
}
