package com.toni.virtualpel.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionType {
    FEED("Feed" , "🍖"),
    PLAY("Play", "🎾"),
    REST("Rest", "💤"),
    EVOLVE("Evolve", "✨");

    private final String displayName;
    private final String emoji;
}