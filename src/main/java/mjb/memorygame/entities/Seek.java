package mjb.memorygame.entities;

import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

/** 
 * This Entity is not necessary.
 * We can use the Game entity to determine seeks.
 */
@Entity
public class Seek {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Player seeker;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Player accepter;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Game game;    

    @Column
    private int cards;

    public Seek() {
    }  
    
    public Seek(Player seeker, int cards) {
        setSeeker(seeker);
        setCards(cards);
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

    public Player getSeeker() {
        return seeker;
    }

    public void setSeeker(Player seeker) {
        this.seeker = seeker;
    }

    public Player getAccepter() {
        return accepter;
    }

    public void setAccepter(Player accepter) {
        this.accepter = accepter;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}