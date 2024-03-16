package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class YouTubeController {

  @Autowired
  private YouTubeService youTubeService;

  @GetMapping("/youtube")
  public String youtube2(Model model) {
    List<Map<String, String>> list = JacksonExample.readJson2();
    model.addAttribute("list", list);
    return "youtube";
  }
}
