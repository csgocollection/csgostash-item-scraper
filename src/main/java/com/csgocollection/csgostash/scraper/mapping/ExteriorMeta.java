package com.csgocollection.csgostash.scraper.mapping;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExteriorMeta {
    Exterior exterior;
    String name;
}
