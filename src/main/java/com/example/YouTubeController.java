package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class YouTubeController {

  @Autowired
  private YouTubeService youTubeService;

  @GetMapping("/")
  public String youtube(Model model) {
    String channelId = "UCHbXBo1fQAg7j0D7HKKYHJg";
    List<Map<String, String>> list = youTubeService.readJson(channelId);
    model.addAttribute("list", list);
    return "youtube";
  }
}
