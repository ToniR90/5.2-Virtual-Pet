package com.toni.virtualpet.model.pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Variant {
    MOUNTAIN("Mountain" , "Red" , "❤️🐲" , "Mountain Dragon, strong and full of resistance") ,
    SWAMP("Swamp" , "Black" , "🖤🐉" , "Swamp Dragon, master of the rotten") ,
    FOREST("Forest" , "Green" , "💚🌿" , "Forest Dragon, agile and smart");

    private final String displayName;
    private final String colorName;
    private final String emoji;
    private final String description;
}
