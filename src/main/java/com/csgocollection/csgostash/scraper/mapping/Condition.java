package com.csgocollection.csgostash.scraper.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Condition {
    FACTORY_NEW("FN"),
    MINIMAL_WEAR("MW"),
    FIELD_TESTED("FT"),
    WELL_WORN("WW"),
    BATTLE_SCARRED("BS"),
    NONE("");

    private final String shortName;

    public static Condition fromShortName(String shortName) {
        for (Condition condition : Condition.values()) {
            if (condition.getShortName().equals(shortName)) {
                return condition;
            }
        }
        return NONE;
    }
}
