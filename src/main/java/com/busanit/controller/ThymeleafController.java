package com.busanit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.busanit.domain.BoardDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @GetMapping("/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafExample02(Model model) {
        BoardDTO boardDto = new BoardDTO();
        boardDto.setTitle("테스트 제목1");
        boardDto.setContent("테스트 내용1");
        boardDto.setWriter("테스터1");
        boardDto.setRegDate(LocalDateTime.now());

        model.addAttribute("boardDto", boardDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafExample03(Model model) {
        List<BoardDTO> boardList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            BoardDTO boardDto = new BoardDTO();
            boardDto.setBno(Long.valueOf(i));
            boardDto.setTitle("테스트 제목" + i);
            boardDto.setContent("테스트 내용" + i);
            boardDto.setWriter("테스터" + i);
            boardDto.setRegDate(LocalDateTime.now());

            boardList.add(boardDto);
        }
        model.addAttribute("boardDtoList", boardList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafExample04(Model model) {
        List<BoardDTO> boardList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            BoardDTO boardDto = new BoardDTO();
            boardDto.setBno(Long.valueOf(i));
            boardDto.setTitle("테스트 제목" + i);
            boardDto.setContent("테스트 내용" + i);
            boardDto.setWriter("테스터" + i);
            boardDto.setRegDate(LocalDateTime.now());

            boardList.add(boardDto);
        }
        model.addAttribute("boardDtoList", boardList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex05")
    public String thymeleafExample05() {
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String thymeleafExample06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);

        return "thymeleafEx/thymeleafEx06";
    }
}







