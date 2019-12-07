package mjb.memorygame.entities;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

@Entity
public class Game {

    @Id
    @GeneratedValue
    private long id;


    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Player player1;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Player player2;

    @Column
    private int cardPairCount;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    List<Move> moves;

    @Column
    private int score1;

    @Column
    private int score2;

    @Transient
    List<Integer> board;

    @Column
    private boolean lock;

    @Transient
    private boolean successfulTurn;

    @Transient
    private boolean gameOver;

    @Transient
    private Player winner;

    public Game() {
        setLock(false);
    }

    public Game(Player player1, int cardCount) {
        this(player1);
        setCardPairCount(cardCount);

    }

    public Game(Player player1) {
        this();
        setPlayer1(player1);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCardPairCount() {
        return cardPairCount;
    }

    public void setCardPairCount(int cardPairCount) {
        this.cardPairCount = cardPairCount;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public List<Integer> asList() {
        List<Integer> movesAsList = new ArrayList<Integer>();
        for(Move move : getMoves()) {
            movesAsList.add(move.getCardIndex());
        }
        return movesAsList;
    }

    public void setMovesFromList(List<Integer> movesList) {
        List<Move> moves = new ArrayList<Move>();
        for(Integer cardIndex : movesList) {
            moves.add(new Move(cardIndex));
        }
        setMoves(moves);
    }

    public List<Integer> getBoard() {
        return board;
    }

    public void setBoard(List<Integer> board) {
        this.board = board;
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

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

}