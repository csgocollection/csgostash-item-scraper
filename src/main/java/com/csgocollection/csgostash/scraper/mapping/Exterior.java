package com.csgocollection.csgostash.scraper.mapping;

import lombok.Getter;

@Getter
public enum Exterior {
    GOLD("Knife", "Contraband"),
    RED("Extraordinary", "Covert", "Master"),
    PINK("Exotic", "Classified", "Superior"),
    PURPLE("Remarkable", "Restricted", "Exceptional"),
    BLUE("High Grade", "Mil-Spec", "Distinguished"),
    LIGHT_BLUE("Industrial Grade"),
    NONE;

    private final String[] identifiers;

    Exterior(String... identifiers) {
        this.identifiers = identifiers;
    }

    public static ExteriorMeta fromString(String text) {
        for (Exterior exterior : Exterior.values()) {
            for (String identifier : exterior.getIdentifiers()) {
                if (text.contains(identifier)) {
                    return new ExteriorMeta(exterior, identifier);
                }
            }
        }

        return new ExteriorMeta(NONE, "None");
    }
}
