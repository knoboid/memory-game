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
        MemoryGame memoryGameState = new MemoryGame(size);
        assertEquals(size, memoryGameState.getSize());
        assertEquals(size * 2, memoryGameState.getCards().size());
        assertEquals(size * 2, memoryGameState.getBoardState().size());
        assertEquals(1, memoryGameState.getCurrentPlayer());
        assertFalse(
            "The cards are not still in sequence", 
            isListInSequence(memoryGameState.getCards())
        );
        assertTrue("Each card appears twice", eachCardAppearsTwice(memoryGameState.getCards()));
        System.out.println(memoryGameState.getCards());
        memoryGameState.setCards(generateReverseSequencedCards(size));
        System.out.println(memoryGameState.getCards());
        System.out.println(memoryGameState.getBoardState());
        assertEquals(1, memoryGameState.getCurrentPlayer());
// MOVE 0 p1
        // Make invalid move.
        try {
            memoryGameState.revealCard(1, size*2);
            assertTrue("expecting MemoryGameCardOutOfRangeException", false);
        } catch (MemoryGameCardOutOfRangeException e) {
            // ignore
        }
        assertEquals(1, memoryGameState.getCurrentPlayer());
        assertEquals(0, memoryGameState.getMoveCount());
        // Make another invalid move.
        try {
            memoryGameState.revealCard(1, -1);
            assertTrue("expecting MemoryGameCardOutOfRangeException", false);
        } catch (MemoryGameCardOutOfRangeException e) {
            // ignore
        }
        int cardIndex = 0;
        // Make a valid move!
        assertTrue(0 == memoryGameState.getBoardState().get(cardIndex));
        memoryGameState.revealCard(1, cardIndex);
        assertEquals(1, memoryGameState.getCurrentPlayer());
        assertEquals(1, memoryGameState.getMoveCount());
        // Make invalid move.
        try {
            memoryGameState.revealCard(1, cardIndex);
            assertTrue("expecting MemoryGameCardIsFaceUpException", false);
        } catch (MemoryGameCardIsFaceUpException e) {
            // ignore
        }
        assertEquals(1, memoryGameState.getCurrentPlayer());
        assertEquals(1, memoryGameState.getMoveCount());
        assertScore(memoryGameState, 0, 0);
        cardIndex = 1;
// MOVE 1 p1
        memoryGameState.revealCard(1, cardIndex);

        System.out.println(memoryGameState.getBoardState());
        assertEquals(1, memoryGameState.getCurrentPlayer());
        assertEquals(2, memoryGameState.getMoveCount());
        
        //// Test score and game board!
        assertEquals(18, memoryGameState.faceDownCount());
        assertScore(memoryGameState, 0, 0);

        try {
            memoryGameState.revealCard(2, cardIndex);
            assertTrue("expecting MemoryGameLocked", false);
        } catch (MemoryGameLockedException e) {
            // ignore
        }
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 1, 0);
        assertEquals(1, memoryGameState.getCurrentPlayer());

        try {
            memoryGameState.revealCard(2, 2);
            assertTrue("expecting MemoryGameWrongPlayerException", false);
        } catch (MemoryGameWrongPlayerException e) {
            // ignore
        }
// MOVE 2 p1
        memoryGameState.revealCard(1, 2);
// MOVE 3 p1
        memoryGameState.revealCard(1, 4);
        assertScore(memoryGameState, 1, 0);
        assertEquals(1, memoryGameState.getCurrentPlayer());
        memoryGameState.completeTurn();

        assertScore(memoryGameState, 1, 0);
        assertEquals(2, memoryGameState.getCurrentPlayer());

// MOVES 4 and 5 p2
        memoryGameState.revealCard(2, 2);
        memoryGameState.revealCard(2, 3);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 1, 1);
        assertEquals(16, memoryGameState.faceDownCount());

// MOVES 6 and 7 p2
        memoryGameState.revealCard(2, 8);
        memoryGameState.revealCard(2, 9);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 1, 2);
        assertEquals(14, memoryGameState.faceDownCount());

        memoryGameState.revealCard(2, 18);
        memoryGameState.revealCard(2, 19);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 1, 3);
        assertEquals(12, memoryGameState.faceDownCount());

        memoryGameState.revealCard(2, 10);
        memoryGameState.revealCard(2, 11);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 1, 4);
        assertEquals(10, memoryGameState.faceDownCount());

        memoryGameState.revealCard(2, 4);
        memoryGameState.revealCard(2, 5);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 1, 5);
        assertEquals(8, memoryGameState.faceDownCount());

        memoryGameState.revealCard(2, 6);
        memoryGameState.revealCard(2, 12);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 1, 5);
        assertEquals(8, memoryGameState.faceDownCount());

        memoryGameState.revealCard(1, 12);
        memoryGameState.revealCard(1, 13);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 2, 5);
        assertEquals(6, memoryGameState.faceDownCount());

        memoryGameState.revealCard(1, 14);
        memoryGameState.revealCard(1, 15);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 3, 5);
        assertEquals(4, memoryGameState.faceDownCount());

        memoryGameState.revealCard(1, 16);
        memoryGameState.revealCard(1, 17);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 4, 5);
        assertEquals(2, memoryGameState.faceDownCount());

        memoryGameState.revealCard(1, 6);
        memoryGameState.revealCard(1, 7);
        memoryGameState.completeTurn();
        assertScore(memoryGameState, 5, 5);
        assertEquals(0, memoryGameState.faceDownCount());

        System.out.println(memoryGameState.getBoardState());




        
    }
}