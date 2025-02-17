package ru.bykov.internettelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bykov.internettelegrambot.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
