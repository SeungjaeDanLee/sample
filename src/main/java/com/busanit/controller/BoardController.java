package com.busanit.controller;

import com.busanit.domain.BoardDTO;
import com.busanit.entity.Board;
import com.busanit.entity.BoardAttach;
import com.busanit.repository.BoardAttachRepository;
import com.busanit.repository.BoardRepository;
import com.busanit.repository.ReplyRepository;
import com.busanit.service.BoardAttachService;
import com.busanit.service.BoardService;
import com.busanit.service.ReplyService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    @Value("${com.busanit.upload.path}")    // application.properties의 변수
    private String uploadPath;

    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final BoardAttachRepository boardAttachRepository;

    @GetMapping("/register")
    public String registerForm() {
        return "board/register";
    }

    @PostMapping("/register")
    public String register(BoardDTO boardDto) {
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setWriter(boardDto.getWriter());

        boardRepository.save(board);

        // 첨부파일 저장
        if (boardDto.getFileNames() != null && boardDto.getFileNames().size() > 0) {
            for (int i = 0; i < boardDto.getFileNames().size(); i++) {
                BoardAttach boardAttach = new BoardAttach();
                boardAttach.setFileName(boardDto.getFileNames().get(i));
                boardAttach.setThumbnailName(boardDto.getThumbnailNames().get(i));
                boardAttach.setBoard(board);

                boardAttachRepository.save(boardAttach);
            }
        }

        return "redirect:/board/list";
    }

    @GetMapping("/update")
    public String updateForm(Long bno, Model model) {
        Board board = boardRepository.findByBno(bno);
        List<BoardAttach> attachList = boardAttachRepository.findByBoard_Bno(bno);

        model.addAttribute("board", board);
        model.addAttribute("attachList", attachList);

        return "board/update";
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDto) {
        Board board = new Board();
        board.setBno(boardDto.getBno());
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setWriter(boardDto.getWriter());

        boardRepository.save(board);

        // 첨부파일 저장
        if(boardDto.getFileNames() != null){
            for (int i = 0; i < boardDto.getFileNames().size(); i++) {
                BoardAttach boardAttach = new BoardAttach();
                boardAttach.setFileName(boardDto.getFileNames().get(i));
                boardAttach.setThumbnailName(boardDto.getThumbnailNames().get(i));
                boardAttach.setBoard(board);

                // DB에 없는 데이터만 추가로 등록
                if (boardDto.getAttachNo().size() == 0 ||
                        boardDto.getAttachNo().get(i) == null) {
                    boardAttachRepository.save(boardAttach);
                }
            }
        }

        return "redirect:/board/view?bno=" + board.getBno();
    }

    @GetMapping("/delete")
    public String delete(Long bno) {
        boardRepository.deleteById(bno);

        return "redirect:/board/list";
    }

    @PostMapping("/delete")
    public String delete(BoardDTO boardDTO) {
        // 게시글을 DB에서 삭제(+reply +boardAttach)
        boardRepository.deleteById(boardDTO.getBno());

        // 첨부파일 물리파일 삭제
        if (boardDTO.getFileNames() != null) {
            for (int i = 0; i < boardDTO.getFileNames().size(); i++) {
                removeFile(boardDTO.getFileNames().get(i));
            }
        }

        return "redirect:/board/list";
    }

    private void removeFile(String fileName) {
        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            // 원본 파일 삭제
            File file = new File(uploadPath + File.separator
                    + srcFileName);
            boolean result = file.delete();

            // 썸네일 파일 삭제
            File thumbnail = new File(file.getParent(),
                    "s_" + file.getName());
            result = thumbnail.delete();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<Board> boardList = boardRepository.findAll();
//        model.addAttribute("list", boardList);
//
//        return "board/list";
//    }

//    @GetMapping("/list")
//    public String list(Model model, String keyword,
//                       @PageableDefault(size = 5, sort = "bno",
//                               direction = Sort.Direction.DESC)Pageable pageable) {
//
//        model.addAttribute("list",
//                boardService.getBoardList(pageable));
//
//        return "board/list";
//    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "") String searchType,
                       @RequestParam(defaultValue = "") String keyword,
                       @PageableDefault(size = 5, sort = "bno",
                            direction = Sort.Direction.DESC)Pageable pageable) {

        if (searchType.equals("title")) {
            model.addAttribute("list",
                    boardService.getBoardTitleList(keyword, pageable));
        } else if (searchType.equals("content")) {
            model.addAttribute("list",
                    boardService.getBoardContentList(keyword, pageable));
        } else {
            model.addAttribute("list",
                    boardService.getBoardList(pageable));
        }
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "board/list";
    }

    @GetMapping("/view")
    public String view(Long bno, Model model) {
        Board board = boardRepository.findByBno(bno);
        List<BoardAttach> attachList = boardAttachRepository.findByBoard_Bno(bno);

        model.addAttribute("board", board);
        model.addAttribute("attachList", attachList);
//        model.addAttribute("reply", replyService.getList(bno));

        return "board/view";
    }
}







