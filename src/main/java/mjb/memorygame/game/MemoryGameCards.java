package mjb.memorygame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoryGameCards {

    private List<Integer> cards;

    public MemoryGameCards(int size) {
        newCards(size);
        shuffleCards();
    }

    public MemoryGameCards(List<Integer> cards) {
        this.cards = cards;
    }

    private void newCards(int size) {
        setCards(new ArrayList<Integer>());
        for (int i=1; i <= size * 2; i++) {
            getCards().add((i+1) / 2);
        }
    }

    private void shuffleCards() {
        int size = getSize();
        int swapCardIndex;
        Random random = new Random();
        for (int cardIndex=0; cardIndex < size; cardIndex++) {
            swapCardIndex = random.nextInt(size);
            switchCards(cardIndex, swapCardIndex);
        }
    }

    private void switchCards(int index1, int index2) {
        int card1Value = getCard(index1);
        setCard(index1, getCard(index2));
        setCard(index2, card1Value);
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

    public int getSize() {
        return getCards().size();
    }

    private void setCard(int index, int value) {
        cards.set(index, value);
    }

    public int getCard(int index) {
        return cards.get(index);
    }

    public List<Integer> asList() {
        return cards;
    }

    public String toString() {
        return cards.toString();
    }
    
}