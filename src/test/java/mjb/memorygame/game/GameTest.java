package mjb.memorygame.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import mjb.memorygame.game.exceptions.MemoryGameCardIsFaceUpException;
import mjb.memorygame.game.exceptions.MemoryGameCardOutOfRangeException;
import mjb.memorygame.game.exceptions.MemoryGameLockedException;
import mjb.memorygame.game.exceptions.MemoryGameWrongPlayerException;

public class GameTest {

    private static boolean isListInSequence(List<Integer> list) {
        Integer prevItem = null;
        for(Integer item : list) {
            if (prevItem != null) {
                if (prevItem > item) {
                    return false;
                }
            }
            prevItem = item;
        }
        return true;
    }

    private static boolean eachCardAppearsTwice(List<Integer> list) {
        for (int i = 1; i <= list.size()/2; i++) {
            if (Collections.frequency(list, i) != 2) return false;
        }
        return true;
    }

    private static List<Integer> generateReverseSequencedCards(int size) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i=size; i > 0; i--) {
            list.add(i);
            list.add(i);
        }
        return list;
    }

    private static void assertScore(MemoryGame gameState, int player1, int player2) {
        assertEquals(player1, gameState.getPlayer1Score());
        assertEquals(player2, gameState.getPlayer2Score());
    }

    @Test
    public void shouldCreateNewGame() {
        int size = 10;
// Initialize game.
        MemoryGame memoryGame = new MemoryGame(size);
        assertEquals(size, memoryGame.getSize());
        assertEquals(size * 2, memoryGame.getCards().getSize());
        assertEquals(size * 2, memoryGame.getBoard().getSize());
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertFalse(
            "The cards are not still in sequence", 
            isListInSequence(memoryGame.getCards().asList())
        );
        assertTrue("Each card appears twice", eachCardAppearsTwice(memoryGame.getCards().asList()));
        // Put the cards in a known order so that we can play a game.
        memoryGame.setCards(generateReverseSequencedCards(size));
        // System.out.println(memoryGame.getCards());
        // System.out.println(memoryGame.getBoard());
        assertEquals(1, memoryGame.getCurrentPlayer());
// MOVE 0 p1
        // Make invalid move.
        try {
            memoryGame.revealCard(1, size*2);
            assertTrue("expecting MemoryGameCardOutOfRangeException", false);
        } catch (MemoryGameCardOutOfRangeException e) {
            // ignore
        }
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertEquals(0, memoryGame.getMoveCount());
        // Make another invalid move.
        try {
            memoryGame.revealCard(1, -1);
            assertTrue("expecting MemoryGameCardOutOfRangeException", false);
        } catch (MemoryGameCardOutOfRangeException e) {
            // ignore
        }
        int cardIndex = 0;
        // Make a valid move!
        assertTrue(0 == memoryGame.getBoard().get(cardIndex));
        memoryGame.revealCard(1, cardIndex);
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertEquals(1, memoryGame.getMoveCount());
        // Make invalid move.
        try {
            memoryGame.revealCard(1, cardIndex);
            assertTrue("expecting MemoryGameCardIsFaceUpException", false);
        } catch (MemoryGameCardIsFaceUpException e) {
            // ignore
        }
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertEquals(1, memoryGame.getMoveCount());
        assertScore(memoryGame, 0, 0);
        cardIndex = 1;
// MOVE 1 p1
        memoryGame.revealCard(1, cardIndex);

        // System.out.println(memoryGame.getBoard());
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertEquals(2, memoryGame.getMoveCount());
        
        //// Test score and game board!
        assertEquals(18, memoryGame.getBoard().hiddenCount());
        assertScore(memoryGame, 0, 0);

        try {
            memoryGame.revealCard(2, cardIndex);
            assertTrue("expecting MemoryGameLocked", false);
        } catch (MemoryGameLockedException e) {
            // ignore
        }
        memoryGame.completeTurn();
        assertScore(memoryGame, 1, 0);
        assertEquals(1, memoryGame.getCurrentPlayer());

        try {
            memoryGame.revealCard(2, 2);
            assertTrue("expecting MemoryGameWrongPlayerException", false);
        } catch (MemoryGameWrongPlayerException e) {
            // ignore
        }
// MOVE 2 p1
        memoryGame.revealCard(1, 2);
// MOVE 3 p1
        memoryGame.revealCard(1, 4);
        assertScore(memoryGame, 1, 0);
        assertEquals(1, memoryGame.getCurrentPlayer());
        memoryGame.completeTurn();

        assertScore(memoryGame, 1, 0);
        assertEquals(2, memoryGame.getCurrentPlayer());

// MOVES 4 and 5 p2
        memoryGame.revealCard(2, 2);
        memoryGame.revealCard(2, 3);
        memoryGame.completeTurn();
        assertScore(memoryGame, 1, 1);
        assertEquals(16, memoryGame.getBoard().hiddenCount());

// MOVES 6 and 7 p2
        memoryGame.revealCard(2, 8);
        memoryGame.revealCard(2, 9);
        memoryGame.completeTurn();
        assertScore(memoryGame, 1, 2);
        assertEquals(14, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(2, 18);
        memoryGame.revealCard(2, 19);
        memoryGame.completeTurn();
        assertScore(memoryGame, 1, 3);
        assertEquals(12, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(2, 10);
        memoryGame.revealCard(2, 11);
        memoryGame.completeTurn();
        assertScore(memoryGame, 1, 4);
        assertEquals(10, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(2, 4);
        memoryGame.revealCard(2, 5);
        memoryGame.completeTurn();
        assertScore(memoryGame, 1, 5);
        assertEquals(8, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(2, 6);
        memoryGame.revealCard(2, 12);
        memoryGame.completeTurn();
        assertScore(memoryGame, 1, 5);
        assertEquals(8, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(1, 12);
        memoryGame.revealCard(1, 13);
        memoryGame.completeTurn();
        assertScore(memoryGame, 2, 5);
        assertEquals(6, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(1, 14);
        memoryGame.revealCard(1, 15);
        memoryGame.completeTurn();
        assertScore(memoryGame, 3, 5);
        assertEquals(4, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(1, 16);
        memoryGame.revealCard(1, 17);
        memoryGame.completeTurn();
        assertScore(memoryGame, 4, 5);
        assertEquals(2, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(1, 6);
        memoryGame.revealCard(1, 7);
        memoryGame.completeTurn();
        assertScore(memoryGame, 5, 5);
        assertEquals(0, memoryGame.getBoard().hiddenCount());

    }
}