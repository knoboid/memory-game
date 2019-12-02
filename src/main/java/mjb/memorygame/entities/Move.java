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
    private long cardIndex;

    public Move() {}
    
    public Move(long cardIndex) {
        setCardIndex(cardIndex);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(long cardIndex) {
        this.cardIndex = cardIndex;
    }
    
}