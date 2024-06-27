package org.example.view;

import org.example.domain.entity.Board;
import org.springframework.context.ApplicationEvent;

public class ViewsEvent extends ApplicationEvent {
    public Board board;

    public ViewsEvent(Board board) {
        super(board);
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
