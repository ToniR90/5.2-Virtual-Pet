package com.toni.virtualpel.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Stage {
    EGG("Egg" , 0 , 2) ,
    YOUNG("Young" , 3 , 5) ,
    ADULT("Adult" , 6 , 9) ,
    ANCIENT("Ancient" , 10 , Integer.MAX_VALUE);

    private final String displayName;
    private final int minExperience;
    private final int maxExperience;

    public static Stage getStageForExperience(int experience) {
        for (Stage stage : values()) {
            if (experience >= stage.minExperience && experience <= stage.maxExperience) {
                return stage;
            }
        }
        return ANCIENT;
    }
}