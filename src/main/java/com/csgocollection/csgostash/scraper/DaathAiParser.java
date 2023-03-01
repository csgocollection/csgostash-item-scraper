package com.csgocollection.csgostash.scraper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DaathAiParser {

    public String parseHtml(Document content) {
        JsonObject json = new JsonObject();

        JsonArray targets = new JsonArray();
        targets.add( content.toString().split("skin")[0]);

        json.add("targets", targets);
        json.addProperty("openai_key", "");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://ai.kagermanov.com/classify"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return httpResponse.body();
        } catch (Exception e) {
            throw new RuntimeException("Error while sending request to Daath AI", e);
        }
    }

}
