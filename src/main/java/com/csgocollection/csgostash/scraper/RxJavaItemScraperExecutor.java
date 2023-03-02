package com.csgocollection.csgostash.scraper;

import com.csgocollection.csgostash.scraper.mapping.Item;
import com.csgocollection.csgostash.scraper.scraping.DocumentItemScraper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class RxJavaItemScraperExecutor {

    private final DocumentItemScraper documentItemScraper;
    private final ItemScraperConfig config;

    public Observable<Item> execute() {
        Observable<Set<String>> links = getLinkSubscriptions("/weapon/")
                .map(document -> document.select("a[href^='" + config.csgostashHost() + "/skin/']")
                        .stream()
                        .map(element -> element.attr("href"))
                        .collect(Collectors.toSet())
                );

        return links
                .flatMap(skinLinks -> Observable.fromIterable(skinLinks)
                        .flatMap(link -> Observable.fromCallable(() -> Jsoup.connect(link).get())
                                .subscribeOn(Schedulers.io()))
                        .map(documentItemScraper::scrape)
                );
    }

    public Observable<Document> getLinkSubscriptions(String linkSuffix) {
        try {
            Elements anchorTags = Jsoup.connect(config.csgostashHost()).get().select("a[href]");

            return Observable.merge(anchorTags.stream()
                    .filter(anchorTag -> anchorTag.attr("href").startsWith(config.csgostashHost() + linkSuffix))
                    .map(anchorTag -> anchorTag.attr("href"))
                    .map(link -> Observable.fromCallable(() -> Jsoup.connect(link).get())).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
