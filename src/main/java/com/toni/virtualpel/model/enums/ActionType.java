package com.toni.virtualpel.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionType {
    FEED("Feed" , "🍖" , "Feed your Dragons to increase their hunger and happiness"),
    PLAY("Play", "🎾" , "Play with your Dragons to increase their happiness"),
    REST("Rest", "💤" , "Let your Dragons rest to refill their energy"),
    IGNORE("Ignore" , "😴" , "Ignore your Dragons to reduce their stats"),
    EVOLVE("Evolve", "✨" , "Evolve your Dragons to their next stage!");

    private final String displayName;
    private final String emoji;
    private final String description;

    public int getExperienceReward() {
        return switch (this) {
            case FEED, PLAY, REST -> 1;
            case IGNORE, EVOLVE -> 0;
        };
    }
}