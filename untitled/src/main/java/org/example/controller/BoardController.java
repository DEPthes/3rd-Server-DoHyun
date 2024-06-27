package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.DTO.BoardDto;
import org.example.domain.entity.Board;
import org.example.domain.entity.Member;
import org.example.service.BoardServiceImpl;
import org.example.view.ViewsEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/board")
    public String Board(Model model, HttpSession session){
        getSession(model, session);
        List<Board> boardAll = boardServiceImpl.findAll();
        model.addAttribute("boardAll", boardAll);

        return "board";
    }

    @GetMapping("/boardWrit")
    public String write(Model model, HttpSession session){
        getSession(model, session);
        model.addAttribute("board", new BoardDto());
        return "boardWrit";
    }

    @PostMapping("/boardWrit")
    @ResponseBody
    public ResponseEntity<?> writing(@Valid @RequestBody BoardDto boardDto, BindingResult result){
        try {
            if (result.hasErrors()) {
                Map<String, String> errorMessage = new HashMap<>();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
            boardServiceImpl.save(boardDto);

            return ResponseEntity.ok("성공");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생");
        }
    }

    @GetMapping("/board/{boardId}")
    public String board(@PathVariable Long boardId, Model model, HttpSession session){
        Board byBoardId = boardServiceImpl.findByBoardId(boardId);
        eventPublisher.publishEvent(new ViewsEvent(byBoardId));

        getSession(model, session);

        model.addAttribute("board", byBoardId);
        return "boardInfo";
    }

    private static void getSession(Model model, HttpSession session){ //로그인 사용자 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
    }

    @GetMapping("/board/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model){
        Board byBoardId = boardServiceImpl.findByBoardId(id);

        model.addAttribute("board", byBoardId);
        return "updateBoard";
    }

    @PutMapping("/board/update")
    @ResponseBody
    public ResponseEntity<?> updateBoardAfter(@RequestBody BoardDto boardDto){
        try{
            Board board = boardServiceImpl.updateBoard(boardDto);
            return ResponseEntity.ok(board);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생");
        }
    }

    @PostMapping("/password/verify")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@RequestBody BoardDto boardDto){
        try {
            String password = boardDto.getPassword();
            String dtoUsername = boardDto.getMemberDto().getUsername();
            Long boardId = boardDto.getId();

            Integer integer = boardServiceImpl.passwordVerify(boardId, dtoUsername, password);

            if (integer == 2){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록한 사용자만 수정할수 있습니다.");
            } else if (integer == 1) {
                return ResponseEntity.ok(integer);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생");
        }
    }

}
