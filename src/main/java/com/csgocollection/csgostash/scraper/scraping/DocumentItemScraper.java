package com.csgocollection.csgostash.scraper.scraping;

import com.csgocollection.csgostash.scraper.mapping.Condition;
import com.csgocollection.csgostash.scraper.mapping.Item;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.stream.Collectors;

public class DocumentItemScraper {

    public static final String YOUTUBE_PREFIX = "https://youtube.com/watch?v=";
    private static final String STEAM_INSPECT_PREFIX = "steam://rungame/730/";

    public Item scrape(Document document) {
        String skinName = document.select("h2").text();
        String description = document.select("p").stream()
                .filter(element -> element.text().startsWith("Description:"))
                .map(element -> element.text().replace("Description: ", ""))
                .findFirst()
                .orElse(null);

        String flavorText = document.select("p").stream()
                .filter(element -> element.text().startsWith("Flavor Text:"))
                .map(element -> element.text().replace("Flavor Text: ", ""))
                .findFirst()
                .orElse(null);

        String finishStyle = document.select("p").stream()
                .filter(element -> element.text().startsWith("Finish Style:"))
                .map(element -> element.text().replace("Finish Style: ", ""))
                .findFirst()
                .orElse(null);
        String finishCatalog = document.select("p").stream()
                .filter(element -> element.text().startsWith("Finish Catalog:"))
                .map(element -> element.text().replace("Finish Catalog: ", ""))
                .findFirst()
                .orElse(null);

        Set<Item.InspectLink> inspectLinks = document.select("a[href]").stream()
                .filter(anchorTag -> anchorTag.attr("href").startsWith(STEAM_INSPECT_PREFIX))
                .map(anchorTag -> {
                    String inspectLink = anchorTag.attr("href");
                    String condition = anchorTag.text().replace("Inspect (", "").replace(")", "");

                    return Item.InspectLink.builder().condition(Condition.fromShortName(condition)).link(inspectLink).build();
                }).collect(Collectors.toSet());
        String previewVideoUrl = YOUTUBE_PREFIX + document.select("div.yt-player-wrapper").attr("data-youtube");

        Item build = Item.builder()
                .name(skinName)
                .description(description)
                .flavorText(flavorText)
                .inspectLinks(inspectLinks)
                .previewVideoUrl(previewVideoUrl)
                .finishStyle(finishStyle)
                .finishCatalog(finishCatalog)
                .build();

        System.out.println(build);
        return build;
    }

}
