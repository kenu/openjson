package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JacksonExample {
  public static void main(String[] args) {
    String json = readFromFile("activities.json");
    readJson(json);
  }

  public static List<Map<String, String>> readJson2() {
    String json = readFromFile("activities.json");
    return readJson(json);
  }

  private static List<Map<String, String>> readJson(String json) {

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(json);
      JsonNode itemsNode = rootNode.get("items");

      // Iterate over the items array
      AtomicInteger index = new AtomicInteger();
      List<Map<String, String>> list = new ArrayList<>();
      for (JsonNode item : itemsNode) {
        System.out.println(item);
        Map<String, String> snippet = getSnippet(item);
        JsonNode upload = item.get("contentDetails").get("upload");
        if (upload == null) {
          continue;
        }
        snippet.put("videoId", String.valueOf(upload.get("videoId")));
        list.add(snippet);
      }
      return list;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }

  private static Map<String, String> getSnippet(JsonNode item) {
    Map<String, String> map = new HashMap<>();
    map.put("title", getTitle(item));
    map.put("thumbnail", getThumbnailMedium(item));
    map.put("publishedAt", getPublishedAt(item));
    return map;
  }

  private static String getPublishedAt(JsonNode item) {
    return Optional.ofNullable(item.get("snippet").get("publishedAt")).map(Object::toString).orElse("");
  }

  private static String getTitle(JsonNode item) {
    JsonNode snippet = item.get("snippet");
    if (snippet == null) {
      return "";
    }
    JsonNode title = snippet.get("title");
    if (title == null) {
      return "";
    }
    return title.toString();
  }

  private static String getThumbnailMedium(JsonNode item) {
    String thumbnail = getThumbnail(item);
    return thumbnail;
  }

  private static String getThumbnail(JsonNode item) {
    return item.get("snippet").get("thumbnails").get("default").get("url").toString();
  }

  public static String readFromFile(String file) {
    try {
      return new String(Files.readAllBytes(Paths.get(file)));
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
