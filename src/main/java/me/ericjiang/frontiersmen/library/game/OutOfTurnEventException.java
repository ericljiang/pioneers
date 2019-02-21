package me.ericjiang.frontiersmen.library.game;

public class OutOfTurnEventException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OutOfTurnEventException(String message) {
        super(message);
    }

}
