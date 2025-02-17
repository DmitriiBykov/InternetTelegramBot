package ru.bykov.internettelegrambot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@Slf4j
@Component
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public void handleSQLException(SQLException e) {
        log.error("Ошибка в базе данных", e);
    }

}
