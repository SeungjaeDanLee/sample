package com.busanit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LayoutController {

    @GetMapping("/layoutEx")
    public String layoutEx() {
        return "layoutEx";
    }

    @GetMapping("/boardEx")
    public String boardEx() {
        return "boardEx";
    }
}

