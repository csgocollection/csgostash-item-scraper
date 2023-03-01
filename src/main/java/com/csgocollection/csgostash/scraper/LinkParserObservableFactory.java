package com.csgocollection.csgostash.scraper;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jsoup.Jsoup;

public class LinkParserObservableFactory {

    public Observable<String> getLinks(String linkSuffix) {
        return Observable.fromCallable(() -> Jsoup.connect(Fixtures.CSGOSTASH_HOST).get().select("a[href]"))
                .subscribeOn(Schedulers.io())
                .flatMapIterable(anchorTags -> anchorTags)
                .filter(anchorTag -> anchorTag.attr("href").startsWith(Fixtures.CSGOSTASH_HOST + linkSuffix))
                .map(anchorTag -> anchorTag.attr("href"));
    }

}
