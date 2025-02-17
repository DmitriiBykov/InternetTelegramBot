package ru.bykov.internettelegrambot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//идентификатор платежа
    private Integer roomNumber;//номер комнаты
    private Long telegramUserId;//id пользователя
    private Double amount;//сумма платежа
    private boolean success;//результат платежа
    private String errorMessage;//ошибка платежа
    private LocalDateTime processedAt;//время обработки платежа
}
