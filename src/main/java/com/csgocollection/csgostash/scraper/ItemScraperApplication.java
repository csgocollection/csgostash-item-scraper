package com.csgocollection.csgostash.scraper;

import com.csgocollection.csgostash.scraper.mapping.Condition;
import com.csgocollection.csgostash.scraper.mapping.Item;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.stream.Collectors;

public class ItemScraperApplication {

    public static void main(String[] args) {
        getLinks("/weapon/")
                .flatMap(link -> Observable.fromCallable(() -> Jsoup.connect(link).get()))
                .flatMapIterable(document -> document.select("a[href^='https://csgostash.com/skin/']")
                        .stream()
                        .map(element -> element.attr("href"))
                        .collect(Collectors.toSet())
                )
                .map(link -> {
                    Document skinDocument = Jsoup.connect(link).get();
                    String skinName = skinDocument.select("h2").text();

                    Set<Item.InspectLink> inspectLinks = skinDocument.select("a[href]").stream()
                            .filter(anchorTag -> anchorTag.attr("href").startsWith("steam://rungame/730/"))
                            .map(anchorTag -> {
                                String inspectLink = anchorTag.attr("href");
                                String condition = anchorTag.text().replace("Inspect (", "").replace(")", "");

                                return Item.InspectLink.builder().condition(Condition.fromShortName(condition)).link(inspectLink).build();
                            }).collect(Collectors.toSet());

                    return Item.builder().name(skinName).inspectLinks(inspectLinks).build();
                })
                .blockingIterable()
                .forEach(System.out::println);
    }

    public static Observable<String> getLinks(String linkSuffix) {
        return Observable.fromCallable(() -> Jsoup.connect(Fixtures.CSGOSTASH_HOST).get().select("a[href]"))
                .subscribeOn(Schedulers.io())
                .flatMapIterable(anchorTags -> anchorTags)
                .filter(anchorTag -> anchorTag.attr("href").startsWith(Fixtures.CSGOSTASH_HOST + linkSuffix))
                .map(anchorTag -> anchorTag.attr("href"));
    }

}
