package ru.bykov.internettelegrambot.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomStatus {
    private String roomName;
    private String roomStatus;
    private Double balance;
    private boolean isActiveBlock;
}
