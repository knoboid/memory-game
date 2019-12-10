package mjb.memorygame.entities;

import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;

// @TODO    Remove this Entity. 
@Entity
public class Seek extends BaseEntity {

    @OneToOne(fetch=FetchType.EAGER)
    private Player seeker;

    @OneToOne(fetch=FetchType.EAGER)
    private Player accepter;

    @OneToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
    private Game game;    

    @Column
    private int cards;

    public Seek() {
    }  
    
    public Seek(Player seeker, int cards) {
        setSeeker(seeker);
        setCards(cards);
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