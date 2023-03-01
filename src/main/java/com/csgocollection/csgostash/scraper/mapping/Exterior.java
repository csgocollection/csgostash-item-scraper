package com.csgocollection.csgostash.scraper.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exterior {
    CONTRABAND("Contraband"),
    KNIVES("Knives"),
    COVERT("Covert"),
    CLASSIFIED("Classified"),
    RESTRICTED("Restricted"),
    MIL_SPEC("Mil-Spec"),
    INDUSTRIAL_GRADE("Industrial Grade"),
    CONSUMER_GRADE("Consumer Grade");

    private String name;
}
