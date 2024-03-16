package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class YouTubeController {

  @Autowired
  private YouTubeService youTubeService;

  @GetMapping("/youtube")
  public String youtube(Model model) {
    // file exists, read the file and return the result
    String activities = youTubeService.readFromFile();
    if (!StringUtils.hasLength(activities)) {
      activities = youTubeService.getFromApi();
    }
    model.addAttribute("message1", activities);
    return "youtube";
  }

  @GetMapping("/youtube2")
  public String youtube2(Model model) {
    List<String> list = JacksonExample.readJson2();
    model.addAttribute("list", list);
    return "youtube2";
  }
}
