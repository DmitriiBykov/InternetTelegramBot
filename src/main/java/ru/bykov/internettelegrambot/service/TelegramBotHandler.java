package ru.bykov.internettelegrambot.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bykov.internettelegrambot.models.RoomStatus;

@Component
@Slf4j
@Setter
public class TelegramBotHandler extends TelegramLongPollingBot {

    public TelegramBotHandler(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private InternetProviderClient internetProviderClient;

    private void sendMessage(Long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText(text);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/pay")) {
                handlePayCommand(messageText, chatId);
            } else if (messageText.startsWith("/status")) {
                handleStatusCommand(messageText, chatId);
            } else {
                sendMessage(chatId, "Неизвестная команда. Используй /pay <room> <sum> или /status <room>");
            }
        }
    }

    private void handlePayCommand(String message, Long chatId) {
        // /pay 123 500
        String[] parts = message.split("\\s+");
        if (parts.length < 3) {
            sendMessage(chatId, "Формат: /pay <room> <amount>");
            return;
        }
        try {
            Integer room = Integer.parseInt(parts[1]);
            Double amount = Double.parseDouble(parts[2]);

            try {
                paymentService.payAndEnableInternet(room, amount, chatId);
            } catch (Exception e) {
                sendMessage(chatId, "Запрос на оплату комнаты " + room + " на сумму " + amount + " не обработан. Проверьте правильность данных или обратитесь к администратору");
            }
            sendMessage(chatId, "Запрос на оплату комнаты " + room + " на сумму " + amount + " обработан. Проверяю статус...");

            // Можно сразу проверить статус (или подождать)
            RoomStatus status = internetProviderClient.getStatus(room);
            if (!status.isActiveBlock()) {
                sendMessage(chatId, "Интернет для комнаты " + room + " включен. Текущий баланс: " + status.getBalance());
            } else
            if(status.getBalance() < amount){
                sendMessage(chatId, "Текущий баланс: " + status.getBalance());
            }
            else
            {
                sendMessage(chatId, "Возможно произошла ошибка при включении. Текущий баланс: " + status.getBalance());
            }

        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат числа. Пример: /pay 123 500");
        }
    }

    private void handleStatusCommand(String message, Long chatId) {

        String[] parts = message.split("\\s+");
        if (parts.length < 2) {
            sendMessage(chatId, "Формат: /status <room>");
            return;
        }
        try {
            Integer room = Integer.parseInt(parts[1]);
            RoomStatus status = internetProviderClient.getStatus(room);
            sendMessage(chatId, String.format("Комната %d, Баланс: %.2f, Интернет: %s",
                    room, status.getBalance(), status.isActiveBlock() ? "Включен" : "Отключен"));
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат номера комнаты. Пример: /status 123");
        }
    }


    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public String getBotUsername() {
        return "internet4leti_bot";
    }
}
