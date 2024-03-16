package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JacksonExample {
  public static void main(String[] args) {
    readJson();
  }

  public static List<String> readJson2() {
    return readJson();
  }
  private static List<String> readJson() {
    String json = readFromFile("activities.json");

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(json);
      JsonNode itemsNode = rootNode.get("items");

      // Iterate over the items array
      AtomicInteger index = new AtomicInteger();
      List<String> list = new ArrayList<>();
      for (JsonNode item : itemsNode) {
        System.out.println(item);
        String snippet = getSnippet(item);
        list.add(snippet);
      }
      return list;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String getSnippet(JsonNode item) {
    return item.get("snippet").fields().next().getValue().asText();
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
