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

    private static List<Integer> generateReverseSequencedCards(int cardPairCount) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i=cardPairCount; i > 0; i--) {
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
    public void shouldCreateNewGame()
            throws MemoryGameWrongPlayerException, MemoryGameCardIsFaceUpException, MemoryGameLockedException,
            MemoryGameCardOutOfRangeException {
        int cardPairCount = 10;
        
        /**
         * Initialize the game.
         */
        MemoryGame memoryGame = new MemoryGame(cardPairCount);
        assertEquals(cardPairCount, memoryGame.getPairCount());
        assertEquals(cardPairCount * 2, memoryGame.getCards().getSize());
        assertEquals(cardPairCount * 2, memoryGame.getBoard().getSize());
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertFalse(
            "The cards are not still in sequence", 
            isListInSequence(memoryGame.getCards().asList())
        );
        assertTrue("Each card appears twice", eachCardAppearsTwice(memoryGame.getCards().asList()));
        /**
         * Put the cards in a known order so that we can play a game.
         */
        memoryGame.setCards(generateReverseSequencedCards(cardPairCount));

        assertEquals(1, memoryGame.getCurrentPlayer());
        assertFalse(memoryGame.isSuccessfulTurn());
        assertFalse("Game is not over.", memoryGame.isGameOver());
        /**
         * MOVE 0 p1
         */
        
        /**
         * Make invalid move by attempting to turn a card with an invalid index.
         */
        try {
            memoryGame.revealCard(1, cardPairCount*2);
            assertTrue("expecting MemoryGameCardOutOfRangeException", false);
        } catch (MemoryGameCardOutOfRangeException e) {
            // ignore
        }
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertEquals(0, memoryGame.getMoveCount());


        /**
         * Make invalid move by attempting to turn another card with an invalid index.
         */
        try {
            memoryGame.revealCard(1, -1);
            assertTrue("expecting MemoryGameCardOutOfRangeException", false);
        } catch (MemoryGameCardOutOfRangeException e) {
            // ignore
        }
        int cardIndex = 0;
        /**
         * Make a valid move!
         */
        assertTrue(0 == memoryGame.getBoard().get(cardIndex));
        memoryGame.revealCard(1, cardIndex);
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertEquals(1, memoryGame.getMoveCount());
        /**
         * Make invalid move by attempting to turn over a card 
         * that is already face up.
         */
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

        assertFalse(memoryGame.isSuccessfulTurn());
        /**
         * MOVE 1 p1
         */
        memoryGame.revealCard(1, cardIndex);

        assertEquals(1, memoryGame.getCurrentPlayer());
        assertEquals(2, memoryGame.getMoveCount());
        
        /**
         * Test that the score and the number of face down cards are as expected.
         */
        assertEquals(18, memoryGame.getBoard().hiddenCount());
        assertScore(memoryGame, 0, 0);

        try {
            memoryGame.revealCard(2, cardIndex);
            assertTrue("expecting MemoryGameLocked", false);
        } catch (MemoryGameLockedException e) {
            // ignore
        }
        assertTrue(memoryGame.isSuccessfulTurn());
        memoryGame.completeTurn();
        assertFalse(memoryGame.isSuccessfulTurn());

        assertScore(memoryGame, 1, 0);
        assertEquals(1, memoryGame.getCurrentPlayer());

        try {
            memoryGame.revealCard(2, 2);
            assertTrue("expecting MemoryGameWrongPlayerException", false);
        } catch (MemoryGameWrongPlayerException e) {
            // ignore
        }

        /**
         * MOVE 2 p1
         */
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.revealCard(1, 2);
        assertFalse(memoryGame.isSuccessfulTurn());

        /**
         * MOVE 3 p1
         */
        memoryGame.revealCard(1, 4);
        assertScore(memoryGame, 1, 0);
        assertEquals(1, memoryGame.getCurrentPlayer());
        assertFalse(memoryGame.isSuccessfulTurn());

        memoryGame.completeTurn();
        assertFalse(memoryGame.isSuccessfulTurn());

        assertScore(memoryGame, 1, 0);
        assertEquals(2, memoryGame.getCurrentPlayer());

        /**
         * MOVES 4 and 5 p2
         */
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.revealCard(2, 2);
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.revealCard(2, 3);
        assertTrue(memoryGame.isSuccessfulTurn());
        memoryGame.completeTurn();
        assertFalse(memoryGame.isSuccessfulTurn());
        assertScore(memoryGame, 1, 1);
        assertEquals(16, memoryGame.getBoard().hiddenCount());

        assertEquals(0, memoryGame.getWinner());

        /**
         * MOVES 6 and 7 p2
         **/ 
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
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.revealCard(2, 11);
        assertTrue(memoryGame.isSuccessfulTurn());
        memoryGame.completeTurn();
        assertFalse(memoryGame.isSuccessfulTurn());
        assertScore(memoryGame, 1, 4);
        assertEquals(10, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(2, 4);
        
        /**
         * Recreate a new game using the cards and the moves from the current game.
         */
        MemoryGame memoryGame2 = 
            new MemoryGame(memoryGame.getCardsAsList(), memoryGame.getMovesAsList(), memoryGame.getLock());
        
        /**
         * Check that the boards from these two game instances are the same size, 
         *  and that they contain the same items.
         */
        assertEquals(memoryGame.getBoard().getSize(), memoryGame2.getBoard().getSize());
        for(int i=0; i<memoryGame.getBoard().getSize(); i++) {
            assertEquals(memoryGame.getBoard().get(i), memoryGame2.getBoard().get(i));
        }

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
        assertScore(memoryGame, 2, 5);
        memoryGame.completeTurn();
        assertScore(memoryGame, 3, 5);
        assertEquals(4, memoryGame.getBoard().hiddenCount());

        memoryGame.revealCard(1, 16);
        memoryGame.revealCard(1, 6);
        assertScore(memoryGame, 3, 5);
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.completeTurn();
        assertScore(memoryGame, 3, 5);
        assertEquals(4, memoryGame.getBoard().hiddenCount());

        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.revealCard(2, 16);
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.revealCard(2, 6);
        assertScore(memoryGame, 3, 5);
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.completeTurn();
        assertFalse(memoryGame.isSuccessfulTurn());
        assertScore(memoryGame, 3, 5);
        assertEquals(4, memoryGame.getBoard().hiddenCount());
        assertFalse(memoryGame.isSuccessfulTurn());

        memoryGame.revealCard(1, 16);
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.revealCard(1, 6);
        assertScore(memoryGame, 3, 5);
        assertFalse(memoryGame.isSuccessfulTurn());
        memoryGame.completeTurn();
        assertFalse(memoryGame.isSuccessfulTurn());
        assertScore(memoryGame, 3, 5);
        assertEquals(4, memoryGame.getBoard().hiddenCount());
        assertFalse(memoryGame.isSuccessfulTurn());

        memoryGame.revealCard(2, 16);
        memoryGame.revealCard(2, 17);
        memoryGame.completeTurn();
        assertScore(memoryGame, 3, 6);
        assertEquals(2, memoryGame.getBoard().hiddenCount());

        assertEquals(0, memoryGame.getWinner());

        assertFalse("Game is not over.", memoryGame.isGameOver());
        memoryGame.revealCard(2, 6);
        assertFalse("Game is not over.", memoryGame.isGameOver());
        memoryGame.revealCard(2, 7);
        assertFalse("Game is not over.", memoryGame.isGameOver());
        assertEquals(0, memoryGame.getWinner());
        memoryGame.completeTurn();
        assertEquals(2, memoryGame.getWinner());
        assertTrue("Game is over.", memoryGame.isGameOver());
        assertScore(memoryGame, 3, 7);
        assertEquals(0, memoryGame.getBoard().hiddenCount());
    }
}