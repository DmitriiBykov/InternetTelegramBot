package ru.bykov.internettelegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bykov.internettelegrambot.models.Payment;
import ru.bykov.internettelegrambot.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final InternetProviderClient internetProviderClient;
    private final PaymentRepository paymentRepository;

    public void payAndEnableInternet(Integer roomNumber, Double amount, Long telegramUserId) {
        boolean paid = false;
        Payment payment = new Payment();
        payment.setRoomNumber(roomNumber);
        payment.setTelegramUserId(telegramUserId);
        payment.setAmount(amount);
        try {
            paid = internetProviderClient.payForRoom(roomNumber, amount);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        if(paid){
            paymentRepository.save(payment);
            boolean enabled = internetProviderClient.enableInternet(roomNumber);
            payment.setSuccess(enabled);
            payment.setErrorMessage(enabled ? null : "Error Activating Internet");
            //TODO: добавить обработку ошибок
        }

    }
}
