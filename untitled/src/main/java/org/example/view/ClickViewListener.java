package org.example.view;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.Board;
import org.example.service.BoardServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClickViewListener implements ApplicationListener<ViewsEvent> {
    private final BoardServiceImpl boardService;

    @Override
    public void onApplicationEvent(ViewsEvent event) {
        Board board = event.getBoard();
        boardService.countViews(board.getId());
    }
}
