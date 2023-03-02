package com.csgocollection.csgostash.scraper.exporter;

import com.csgocollection.csgostash.scraper.mapping.Item;

import java.util.Set;

@FunctionalInterface
public interface ItemExporter {
    void exportItems(Set<Item> items);
}
