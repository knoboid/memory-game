package mjb.memorygame.game;

import java.util.ArrayList;
import java.util.List;

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

    private int pairCount;
    private MemoryGameCards cards;
    private MemoryGameBoard board;
    private int firstPlayer;
    private int currentPlayer;
    private int player1Score = 0;
    private int player2Score = 0;
    private List<Integer> moves = new ArrayList<Integer>();
    private boolean lock;
    private boolean successfulTurn;
    private boolean gameOver;
    
    public MemoryGame(int pairCount) {
        this(pairCount, 1);
    }

    public MemoryGame(List<Integer> cards, List<Integer> moves, boolean lock)
            throws MemoryGameWrongPlayerException, MemoryGameCardIsFaceUpException, MemoryGameLockedException,
            MemoryGameCardOutOfRangeException {
        this(cards, moves, lock, 1);
    }

    /**
     * Recreates a game using a given set of cards, a given set of moves and the
     * first player
     * 
     * @param cards
     * @param moves
     * @param firstPlayer - an integer whose only valid values are 1 or 2
     * @throws MemoryGameCardIsFaceUpException
     * @throws MemoryGameWrongPlayerException
     * @throws MemoryGameLockedException
     * @throws MemoryGameCardOutOfRangeException
     */
    public MemoryGame(List<Integer> cards, List<Integer> moves, boolean lock, int firstPlayer) throws MemoryGameWrongPlayerException,
            MemoryGameCardIsFaceUpException, MemoryGameLockedException, MemoryGameCardOutOfRangeException {
        setPairCount(cards.size());
        setFirstPlayer(firstPlayer);
        setCurrentPlayer(firstPlayer);
        this.cards = new MemoryGameCards(cards);
        this.board = new MemoryGameBoard(this.cards);
        setSuccessfulTurn(false);
        setGameOver(false);

        for(int i=0; i < moves.size(); i++) {
            this.revealCard(getCurrentPlayer(), moves.get(i));
            if (isNewTurn() && i != (moves.size() - 1)) {
                completeTurn();
            }
        }
        if (isNewTurn() && !lock && moves.size() > 0) {
            completeTurn();
        }
    }   

    public MemoryGame(int pairCount, int firstPlayer) {
        setPairCount(pairCount);
        // Check firstPlayer is 1 or 2: Better still, always default to 1.
        setFirstPlayer(firstPlayer);
        setCurrentPlayer(firstPlayer);
        this.cards = new MemoryGameCards(pairCount);
        this.board = new MemoryGameBoard(this.cards);
        setSuccessfulTurn(false);
        setGameOver(false);
    }

    public void revealCard(int player, int cardIndex) throws MemoryGameWrongPlayerException, MemoryGameCardIsFaceUpException, MemoryGameLockedException, MemoryGameCardOutOfRangeException {
        checkLock();
        checkValidPlayer(player);
        int cardState = checkRangeThenReturnCardValue(cardIndex);
        doMove(cardIndex, cardState);
    }

    private void checkLock() throws MemoryGameLockedException {
        if (getLock()) {
            String message = "Game locked. Don't forget to finish each turn by calling completeTurn()";
            throw new MemoryGameLockedException(message);
        }
    }

    private void checkValidPlayer(int player) throws MemoryGameWrongPlayerException {
        if (player != getCurrentPlayer()) {
            String message = String.format("It is not player %d's turn!", player);
            throw new MemoryGameWrongPlayerException(message);
        }
    }

    private int checkRangeThenReturnCardValue(int cardIndex) throws MemoryGameCardOutOfRangeException {
        try{
            return getBoard().get(cardIndex);
        } catch (IndexOutOfBoundsException e){
            throw new MemoryGameCardOutOfRangeException(e.getMessage());
        }
    }

    private void doMove(int cardIndex, int cardState) throws MemoryGameCardIsFaceUpException {
        if (cardState != 0) {
            throw new MemoryGameCardIsFaceUpException("The card is already face up!");
        }
        else {
            getBoard().reveal(cardIndex);
            this.addMove(cardIndex);
            if (isNewTurn()) {
                // int mc = getMoveCount();
                setLock(true);
                if (cardsMatch(getMoveCount() - 1)) {
                    setSuccessfulTurn(true);
                }
            } 
        }
    }

    private boolean isNewTurn() {
        return (getMoveCount() % 2) == 0;
    }

    private boolean cardsMatch(int moveIndex) {
        int card1 = getCard(getCardIndexFromMoveIndex(moveIndex));
        int card2 = getCard(getCardIndexFromMoveIndex(moveIndex - 1));
        return card1 == card2;
    }

    private void checkScore(int moveIndex) {
        if (cardsMatch(moveIndex)) {
            playerIncrementScore(getCurrentPlayer());
        }
        else {
            getBoard().hide(getCardIndexFromMoveIndex(moveIndex));
            getBoard().hide(getCardIndexFromMoveIndex(moveIndex - 1));
            toggleCurrentPlayer();
        }
    }

    private void playerIncrementScore(int player) {
        if (player == 1) {
            setPlayer1Score(getPlayer1Score() + 1);
        }
        else if (player == 2) {
            setPlayer2Score(getPlayer2Score() + 1);
        }
        else {
            return;
        }
    }

    public void completeTurn() {
        checkScore(getMoveCount()-1);
        setLock(false);
        setSuccessfulTurn(false);
        if (getBoard().hiddenCount() == 0) {
            setGameOver(true);
        }
    }
    
    /************************
    *  Getters And Setters  *
    ************************/
    public int getPairCount() {
        return pairCount;
    }

    public void setPairCount(int pairCount) {
        this.pairCount = pairCount;
    }

    public MemoryGameCards getCards() {
        return cards;
    }

    public List<Integer> getCardsAsList() {
        return getCards().asList();
    }

    public void setCards(List<Integer> cards) {
        this.cards.setCards(cards);
    }

    public MemoryGameBoard getBoard() {
        return board;
    }

    public List<Integer> getBoardAsList() {
        return getBoard().asList();
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

    public List<Integer> getMoves() {
        return moves;
    }

    public List<Integer> getMovesAsList() {
        return moves;
    }

    public void addMove(int cardIndex) {
        this.getMoves().add(cardIndex);
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

    public void toggleCurrentPlayer() {
        setCurrentPlayer(3 - getCurrentPlayer());
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private int getCardIndexFromMoveIndex(int moveIndex) {
        return getMoves().get(moveIndex);
    }

    private int getCard(int cardIndex) {
        return getCards().getCard(cardIndex);
    }

    public boolean getLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isSuccessfulTurn() {
        return successfulTurn;
    }

    public void setSuccessfulTurn(boolean successfulTurn) {
        this.successfulTurn = successfulTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

}