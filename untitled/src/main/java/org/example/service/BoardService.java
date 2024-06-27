package org.example.service;

import org.example.domain.DTO.BoardDto;
import org.example.domain.entity.Board;

import java.util.List;

public interface BoardService {
    Board save(BoardDto boardDto);
    List<Board> findAll();
    Board findByBoardId(Long boardId);
    Board updateBoard(BoardDto boardDto);
}
