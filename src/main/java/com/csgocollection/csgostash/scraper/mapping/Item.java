package com.csgocollection.csgostash.scraper.mapping;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
@Builder
public class Item {
    @Value
    @Builder
    public static class InspectLink {
        Condition condition;
        String link;
    }

    String name;
    String description;
    String flavorText;

    ExteriorMeta exteriorMeta;
    Condition condition;
    Modifier modifier;

    Set<Collection> collections;

    String finishStyle;
    String finishCatalog;

    FloatRange floatRange;
    Map<Condition, FloatRange> floatRangeByCondition;

    PriceRange priceRange;
    Map<Condition, Double> priceByCondition;
    Map<Condition, Double> priceByConditionWithModifiers;

    Set<InspectLink> inspectLinks;
    String texturePatternLink;
    String previewVideoUrl;

    public String getMarketHashName() {
        return (modifier != Modifier.NONE ? " " + modifier : "") + name + " (" + condition + ")";
    }
}
