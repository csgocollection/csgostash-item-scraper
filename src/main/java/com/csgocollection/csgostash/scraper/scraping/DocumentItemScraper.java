package com.csgocollection.csgostash.scraper.scraping;

import com.csgocollection.csgostash.scraper.mapping.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Objects;
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

        Set<Phase> itemPhases = null;
        if (finishCatalog != null && finishCatalog.contains("Multiple")) {
            Set<Phase> phases = new HashSet<>();
            for (Element element : document.select("div.col-md-6.text-center.no-padding.margin-top-large.margin-bot-large")) {
                String name = element.select("h4").text();
                if (name.isEmpty()) {
                    name = element.select("h3").text();
                }

                String phaseFinishCatalogRaw = element.select("p").text();
                String parsedFinishCatalog = phaseFinishCatalogRaw.isEmpty()
                        ? null
                        : element.select("p").text().substring(phaseFinishCatalogRaw.indexOf("#") + 1);

                String inspectLink = element.select("a.inspect-button-skin").attr("href");
                String previewImage = element.select("img.skin-varient-img").attr("src");

                phases.add(new Phase(name.isEmpty() ? null : name, parsedFinishCatalog, inspectLink, previewImage));
            }

            if (!phases.isEmpty()) {
                finishCatalog = phases.iterator().next().finishCatalog();
                itemPhases = phases.stream().filter(phase -> phase.name() != null).collect(Collectors.toSet());
            }
        }

        String quality = document.select("div.quality").text();
        ExteriorMeta exteriorMeta = Exterior.fromString(quality);

        Set<Item.InspectLink> inspectLinks = document.select("a[href]").stream()
                .filter(anchorTag -> anchorTag.attr("href").startsWith(STEAM_INSPECT_PREFIX))
                .map(anchorTag -> {
                    String inspectLink = anchorTag.attr("href");
                    String condition = anchorTag.text().replace("Inspect (", "").replace(")", "");
                    Condition inspectCondition = Condition.fromShortName(condition);

                    if (inspectLink.isEmpty()) {
                        return null;
                    }

                    return Item.InspectLink.builder().condition(inspectCondition).link(inspectLink).build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

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
                .phases(itemPhases)
                .build();

        log.info("Scraped item: {}", item);

        return item;
    }

}
