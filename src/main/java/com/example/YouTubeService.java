package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

  public String getFromApi() {
    String channelId = "UCHbXBo1fQAg7j0D7HKKYHJg";
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
        "https://www.googleapis.com/youtube/v3/activities?part=" + parts + "&maxResults=5&channelId=" + channelId + "&key=" + apiKey;
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForObject(url, String.class);
  }
}
