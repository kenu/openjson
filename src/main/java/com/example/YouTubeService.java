package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class YouTubeService {

  @Value("${youtube.api.key}")
  private String apiKey;

  public static final String CACHE_FILE = "activities.json";

  public String readFromFile() {
    try {
      return new String(Files.readAllBytes(Paths.get(CACHE_FILE)));
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getFromApi(String channelId) {
    String activities = getActivities(channelId);
    // save the result to file
    saveToFile(activities);
    return activities;
  }

  private void saveToFile(String string) {
    try (FileWriter fw = new FileWriter(CACHE_FILE)) {
      fw.write(string);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getActivities(String channelId) {
    String parts = "snippet,contentDetails";
    String url =
        "https://www.googleapis.com/youtube/v3/activities?part=" + parts + "&maxResults=50&channelId=" + channelId + "&key=" + apiKey;
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForObject(url, String.class);
  }

  public List<Map<String, String>> readJson(String channelId) {
    String json = getActivities(channelId);

    try {
      JsonNode rootNode = new ObjectMapper().readTree(json);
      JsonNode itemsNode = rootNode.get("items");

      List<Map<String, String>> list = new ArrayList<>();
      for (JsonNode item : itemsNode) {
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

  private static String getTitle(JsonNode item) {
    JsonNode snippet = getSnippetFromNode(item);
    if (snippet == null) {
      return "";
    }
    return Optional.ofNullable(snippet.get("title")).toString();
  }

  private static JsonNode getSnippetFromNode(JsonNode item) {
    return item.get("snippet");
  }

  private static String getThumbnailMedium(JsonNode item) {
    return getSnippetFromNode(item).get("thumbnails").get("medium").get("url").toString();
  }

  private static String getPublishedAt(JsonNode item) {
    return Optional.ofNullable(getSnippetFromNode(item).get("publishedAt")).map(Object::toString).orElse("");
  }

}
