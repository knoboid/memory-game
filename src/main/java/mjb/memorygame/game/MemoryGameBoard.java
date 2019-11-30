package mjb.memorygame.game;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameBoard {

    private MemoryGameCards cards;
    private List<Integer> board;

    public MemoryGameBoard(MemoryGameCards cards) {
        this.cards = cards;
        newBoard(cards.getSize());
    }

    private void newBoard(int size) {
        board = new ArrayList<Integer>();
        for (int i=1; i <= size; i++) {
            board.add(0);
        }
    }

    public void reveal(int index) {
        set(index, cards.getCard(index));
    }

    public void hide(int index) {
        set(index, 0);
    }

    private boolean isHidden(int index) {
        return get(index) == 0;
    }

    public int hiddenCount() {
        int count = 0;
        for (int i=0; i < board.size(); i++) {
            if (isHidden(i)) {
                count++;
            }
        }
        return count;
    }

    public int getSize() {
        return board.size();
    }

    private void set(int index, int value) {
        board.set(index, value);
    }

    public int get(int index) {
        return board.get(index);
    }

    public List<Integer> getBoard() {
        return board;
    }

    public void setBoard(List<Integer> board) {
        this.board = board;
    }

    public String toString() {
        return board.toString();
    }
    
}