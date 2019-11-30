package mjb.memorygame.game.exceptions;

public class MemoryGameWrongPlayerException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public MemoryGameWrongPlayerException() {
        super();
    }

    public MemoryGameWrongPlayerException(String s) {
        super(s);
    }
    
}