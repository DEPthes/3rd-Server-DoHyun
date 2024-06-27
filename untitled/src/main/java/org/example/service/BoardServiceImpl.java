package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.DTO.BoardDto;
import org.example.domain.entity.Board;
import org.example.domain.entity.Member;
import org.example.repository.BoardRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    @Override
    public Board save(BoardDto boardDto){
        String username = boardDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsername(username);
        Board build = Board.builder()
                .title(boardDto.getTitle())
                .dateTime(LocalDateTime.now())
                .writer(byUsername.getUsername())
                .password(boardDto.getPassword())
                .content(boardDto.getContent())
                .count(0)
                .member(byUsername)
                .views(0)
                .build();

        Board save = boardRepository.save(build);
        return save;
    }

    @Override
    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    @Override
    public Board findByBoardId(Long boardId){
        try{
            return boardRepository.findById(boardId).orElseThrow(()->new RuntimeException("Board Not Found with ID:" + boardId));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while finding Board by ID:", e);
        }
    }

    public Board countViews(Long boardId){
        Board byBoardId = findByBoardId(boardId);
        Integer views = byBoardId.getViews();
        byBoardId.setViews(++views);
        return boardRepository.save(byBoardId);
    }

    @Override
    public Board updateBoard(BoardDto boardDto){
        Board byBoardId = findByBoardId(boardDto.getId());
        byBoardId.update(boardDto.getTitle(), boardDto.getContent());
        return boardRepository.save(byBoardId);
    }

    public Integer passwordVerify(Long boardId, String password, String username) {
        Board byBoardId = findByBoardId(boardId);
        String boardPassword = byBoardId.getPassword();
        Member memberUsername = memberService.findByUsername(username);

        if (!boardPassword.equals(password)){
           if (memberUsername == null || !(byBoardId.getWriter().equals(username))){
               return 2;
           }
           return 1;
        }
        return 0;
    }
}

