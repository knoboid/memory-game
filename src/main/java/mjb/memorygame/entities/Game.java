package mjb.memorygame.entities;

import java.util.List;
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
    private int cards;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    List<Move> moves;

    @Transient
    List<Integer> board;

    public Game() {}

    public Game(Player player1, int cards) {
        setPlayer1(player1);
        setCards(cards);

    }

    public Game(Player player1) {
        setPlayer1(player1);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCards() {
        return cards;
    }

    public void setCards(int cards) {
        this.cards = cards;
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

    public List<Move>  getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public List<Integer> getBoard() {
        return board;
    }

    public void setBoard(List<Integer> board) {
        this.board = board;
    }

}