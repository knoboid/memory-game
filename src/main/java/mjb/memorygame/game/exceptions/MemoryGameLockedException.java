package mjb.memorygame.game.exceptions;


public class MemoryGameLockedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MemoryGameLockedException() {
        super();
    }

    public MemoryGameLockedException(String s) {
        super(s);
    }
}