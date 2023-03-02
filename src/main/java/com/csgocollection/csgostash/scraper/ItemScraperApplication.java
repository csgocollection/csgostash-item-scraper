package com.csgocollection.csgostash.scraper;

import com.csgocollection.csgostash.scraper.exporter.GsonFileItemExporter;
import com.csgocollection.csgostash.scraper.mapping.Item;
import com.csgocollection.csgostash.scraper.scraping.DocumentItemScraper;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ItemScraperApplication {

    public static void main(String[] args) {
        ItemScraperConfig config = new ItemScraperConfig(Fixtures.CSGOSTASH_HOST);
        Iterable<Item> items = new RxJavaItemScraperExecutor(new DocumentItemScraper(), config)
                .execute()
                .blockingIterable();

        new GsonFileItemExporter("exports/")
                .exportItems(StreamSupport.stream(items.spliterator(), false).collect(Collectors.toSet()));
    }

}
