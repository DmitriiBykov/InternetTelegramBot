package ru.bykov.internettelegrambot.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.bykov.internettelegrambot.models.RoomStatus;


@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Setter
@Component
public class InternetProviderClient {
    private RestTemplate restTemplate;
    private String url;

    public boolean payForRoom(Integer roomNumber, Double amount) {
        String payUrl = url + "/pay?room=" + roomNumber + "&sum=" + amount;
        try {
            restTemplate.postForObject(payUrl, null, Boolean.class); //TODO реализовать оплату
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
        return true;
    }

    public boolean enableInternet(Integer roomNumber) {
        String enableUrl = url + "/enable?room=" + roomNumber;
        try {
            restTemplate.postForObject(enableUrl, null, Boolean.class); //TODO реализовать включение
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public RoomStatus getStatus(Integer roomNumber) {
        String statusUrl = url + "/status?room=" + roomNumber;
        log.info("check status of room " + roomNumber);
        try {
            return restTemplate.getForObject(statusUrl, RoomStatus.class); //TODO реализовать получение статуса комнаты
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
