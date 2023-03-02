package com.csgocollection.csgostash.scraper;

public class ItemScraperApplication {

    public static void main(String[] args) {
        new RxJavaItemScraperExecutor(new DocumentItemScraper(), new ItemScraperConfig(Fixtures.CSGOSTASH_HOST))
                .execute()
                .blockingIterable()
                .forEach(System.out::println);
    }

}
