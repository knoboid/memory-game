package mjb.memorygame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mjb.memorygame.game.exceptions.MemoryGameCardIsFaceUpException;
import mjb.memorygame.game.exceptions.MemoryGameCardOutOfRangeException;
import mjb.memorygame.game.exceptions.MemoryGameLockedException;
import mjb.memorygame.game.exceptions.MemoryGameWrongPlayerException;

/**
 * Memory Game is a two player game.
 * 
 * A number of card pairs are shuffled and dealt face down.
 * 
 * Each player takes alternate turns.
 * 
 * During their turn a player is allowed to turn over two face down cards.
 * If the two cards are a matching pair this player scores a point and they 
 * are removed from the game. This player keeps the turn and plays again.
 * 
 * If the two cards do not match they are placed face down again, there is no
 * score and it is the other players turn.
 * 
 * The game finishes when all the cards have been matched and are out of play.
 * 
 * The winner is the player with the highest score
 */
public class MemoryGame {

    private int size;
    private List<Integer> cards;
    private List<Integer> boardState;
    private int firstPlayer;
    private int currentPlayer;
    private int player1Score = 0;
    private int player2Score = 0;
    private List<Integer> moves = new ArrayList<Integer>();
    private boolean lock;
    
    public MemoryGame(int size) {
        this(size, 1);
    }

    public MemoryGame(int size, int firstPlayer) {
        setSize(size);
        // Check firstPlayer is 1 or 2: Better still, always default to 1.
        setFirstPlayer(firstPlayer);
        setCurrentPlayer(firstPlayer);
        newCards();
        shuffleCards();
    }

    private void newCards() {
        setCards(new ArrayList<Integer>());
        setBoardState(new ArrayList<Integer>());
        for (int i=1; i <= getSize(); i++) {
            getCards().add(i);
            getCards().add(i);
            getBoardState().add(0);
            getBoardState().add(0);
        }
    }

    private void shuffleCards() {
        int deckSize = getCards().size();
        int swapCardIndex;
        int swapCardValue;
        Random random = new Random();
        for (int cardIndex=0; cardIndex < deckSize; cardIndex++) {
            swapCardIndex = random.nextInt(deckSize);
            swapCardValue = getCards().get(swapCardIndex);
            getCards().set(swapCardIndex, getCards().get(cardIndex));
            getCards().set(cardIndex, swapCardValue);
        }
    }

    public void revealCard(int player, int index) {
        checkLock();
        checkValidPlayer(player);
        int cardState = checkRangeThenReturnCardValue(index);
        doMove(index, cardState);
    }

    private void checkLock() {
        if (lock) {
            String message = "Game locked. Don't forget to finish each by calling completeMove()";
            throw new MemoryGameLockedException(message);
        }
    }

    private void checkValidPlayer(int player) {
        if (player != getCurrentPlayer()) {
            String message = String.format("It is not player %d's turn!", player);
            throw new MemoryGameWrongPlayerException(message);
        }
    }

    private int checkRangeThenReturnCardValue(int index) {
        try{
            return getBoardState().get(index);
        } catch (IndexOutOfBoundsException e){
            throw new MemoryGameCardOutOfRangeException(e.getMessage());
        }
    }

    private void doMove(int index, int cardState) {
        if (cardState != 0) {
            throw new MemoryGameCardIsFaceUpException("The card is already face up!");
        }
        else {
            getBoardState().set(index, getCards().get(index));
            this.addMove(index);
            if (newTurn()) {
                lock = true;
            } 
        }
    }

    private boolean newTurn() {
        return !isHalfTurn();
    }

    private void checkScore(int index) {
        int card1 = getCards().get(getCardIndexFromMove(index));
        int card2 = getCards().get(getCardIndexFromMove(index - 1));
        if (card1 == card2) {
            playerIncrementScore(getCurrentPlayer());
        }
        else {
            getBoardState().set(getCardIndexFromMove(index), 0);
            getBoardState().set(getCardIndexFromMove(index - 1), 0);
            toggleCurrentPlayer();
        }
    }

    public int faceDownCount() {
        int count = 0;
        for (int item : getBoardState()) {
            if (item==0) {
                count++;
            }
        }
        return count;
    }

    private void playerIncrementScore(int player) {
        if (player == 1) {
            setPlayer1Score(getPlayer1Score() + 1);
        }
        else if (player == 2) {
            setPlayer2Score(getPlayer2Score() + 1);
        }
    }


    public void completeTurn() {
        checkScore(getMoveCount()-1);
        lock = false;
    }
    
    /************************
    *  Getters And Setters  *
    ************************/
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

    public List<Integer> getBoardState() {
        return boardState;
    }

    public void setBoardState(List<Integer> cardState) {
        this.boardState = cardState;
    }

    public int getNextPlayer() {
        int turns = getMoveCount()/2;
        return (turns + getFirstPlayer() + 1) % 2 + 1;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public boolean isHalfTurn() {
        return (getMoveCount() % 2) == 1;
    }

    public List<Integer> getMoves() {
        return moves;
    }

    public void addMove(int index) {
        this.getMoves().add(index);
    }

    public int getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(int firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public int getMoveCount() {
        return getMoves().size();
    }

    // public int getCurrentPlayer() {
    //     int fullTurns = getMoveCount()/2;
    //     return (fullTurns + getFirstPlayer() + 1) % 2 + 1;
    // }

    public void toggleCurrentPlayer() {
        setCurrentPlayer(3 - getCurrentPlayer());
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }    

    public int getPreviousPlayer() {
        return 3 - getCurrentPlayer();
    }

    private int getCardIndexFromMove(int move) {
        // int halfTurns = getMoveCount();
        // int index = getMoves().get(halfTurns - turnsAgo);
        return getMoves().get(move);
    }




}