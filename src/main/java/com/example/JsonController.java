package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class JsonController {
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("message1", "Hello, World!");
    List<String> list = getList();
    model.addAttribute("list", list);
    return "index";
  }

  private List<String> getList() {
    return List.of("BTS", "Blackpink", "GOT7", "TWICE", "Red Velvet");
  }
}
