package com.csgocollection.csgostash.scraper.exporter;

import com.csgocollection.csgostash.scraper.mapping.Item;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GsonFileItemExporter implements ItemExporter {

    private static final Gson GSON = new Gson();

    private final String destinationPath;

    @Override
    public void exportItems(Set<Item> items) {
        String fileName = this.destinationPath + String.format("items-%s.json", System.currentTimeMillis());

        try (Writer writer = new FileWriter(fileName)) {
            GSON.toJson(items.stream().filter(i -> !i.getName().isEmpty()).collect(Collectors.toSet()), writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + fileName, e);
        }
    }
}
