package ru.bykov.internettelegrambot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "rooms")

public class Room {
    @Id
    private Integer roomNumber;
    //TODO добавить комнату если надо(нет)
}
