package mjb.memorygame.entities;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Move {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private int cardIndex;

    public Move() {}
    
    public Move(int cardIndex) {
        setCardIndex(cardIndex);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }
    
}