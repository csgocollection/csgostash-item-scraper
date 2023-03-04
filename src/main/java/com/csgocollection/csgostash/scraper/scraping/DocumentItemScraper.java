package com.csgocollection.csgostash.scraper.scraping;

import com.csgocollection.csgostash.scraper.mapping.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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

        String quality = document.select("div.quality").text();
        ExteriorMeta exteriorMeta = Exterior.fromString(quality);

        Set<Item.InspectLink> inspectLinks = document.select("a[href]").stream()
                .filter(anchorTag -> anchorTag.attr("href").startsWith(STEAM_INSPECT_PREFIX))
                .map(anchorTag -> {
                    String inspectLink = anchorTag.attr("href");
                    String condition = anchorTag.text().replace("Inspect (", "").replace(")", "");

                    return Item.InspectLink.builder().condition(Condition.fromShortName(condition)).link(inspectLink).build();
                }).collect(Collectors.toSet());

        String youtubeWatchParam = document.select("div.yt-player-wrapper").attr("data-youtube");
        String previewVideoUrl = !youtubeWatchParam.isEmpty() ? YOUTUBE_PREFIX + youtubeWatchParam : null;

        boolean souvenirAvailable = document.select("div.souvenir").size() > 0;
        boolean statTrakAvailable = document.select("div.stattrak").size() > 0;

        Modifier modifier = souvenirAvailable ? Modifier.SOUVENIR
                : statTrakAvailable ? Modifier.STAT_TRAK : Modifier.NONE;

        String texturePatternLink = document.select("div.skin-details-previews")
                .select("a")
                .attr("href");
        if (texturePatternLink.isEmpty()) {
            texturePatternLink = null;
        }

        Item item = Item.builder()
                .name(skinName)
                .description(description)
                .exteriorMeta(exteriorMeta)
                .flavorText(flavorText)
                .inspectLinks(inspectLinks)
                .texturePatternLink(texturePatternLink)
                .previewVideoUrl(previewVideoUrl)
                .finishStyle(finishStyle)
                .finishCatalog(finishCatalog)
                .modifier(modifier)
                .build();

        log.info("Scraped item: {}", item);

        return item;
    }

}
