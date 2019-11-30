package mjb.memorygame.game.exceptions;

public class MemoryGameCardOutOfRangeException extends IndexOutOfBoundsException {

    private static final long serialVersionUID = 1L;

    public MemoryGameCardOutOfRangeException() {
        super();
    }

    public MemoryGameCardOutOfRangeException(String s) {
        super(s);
    }

}