package com.csgocollection.csgostash.scraper;

import com.csgocollection.csgostash.scraper.mapping.Item;
import com.csgocollection.csgostash.scraper.scraping.DocumentItemScraper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;

import java.util.stream.Collectors;

@AllArgsConstructor
public class RxJavaItemScraperExecutor {

    private final DocumentItemScraper documentItemScraper;
    private final ItemScraperConfig config;

    public Observable<Item> execute() {
        return getLinks("/weapon/")
                .flatMap(link -> Observable.fromCallable(() -> Jsoup.connect(link).get()))
                .flatMapIterable(document -> document.select("a[href^='" + config.csgostashHost() + "/skin/']")
                        .stream()
                        .map(element -> element.attr("href"))
                        .collect(Collectors.toSet())
                )
                .map(link -> Jsoup.connect(link).get())
                .map(documentItemScraper::scrape);
    }

    public Observable<String> getLinks(String linkSuffix) {
        return Observable.fromCallable(() -> Jsoup.connect(config.csgostashHost()).get().select("a[href]"))
                .subscribeOn(Schedulers.io())
                .flatMapIterable(anchorTags -> anchorTags)
                .filter(anchorTag -> anchorTag.attr("href").startsWith(config.csgostashHost() + linkSuffix))
                .map(anchorTag -> anchorTag.attr("href"));
    }

}
